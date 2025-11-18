package distributed.saga.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import distributed.saga.data.CommandData;
import distributed.saga.data.CommandState;
import distributed.saga.data.Data;
import distributed.saga.data.State;
import distributed.saga.database.DataStore;
import distributed.saga.model.DataService;
import distributed.saga.model.Handler;
import lombok.Setter;

@Setter
@Service
public class Orchestrator {

    private static final Logger log = LoggerFactory.getLogger(Orchestrator.class);

    @Autowired @Qualifier("services") private Map<String, DataService> services;
    @Autowired @Qualifier("executors") private Map<String, ExecutorService> executors;

    @Autowired private Manager manager;

    @Autowired private DataStore dataStore;

    public void startOperation(Data initial) {
        if (initial == null)
            throw new IllegalArgumentException("initial data is null");
        log.info("Starting saga for txn={}", initial.getTransactionID());
        Handler head = manager.getHead();
        processHandler(head, initial);
    }

    private void processHandler(Handler handler, Data data) {
        if (handler == null) {
            log.info("Handler is null — workflow complete for txn={}", data.getTransactionID());
            return;
        }
        CommandData cmd = handler.createCommand(data);
        if (cmd == null) {
            log.warn("Handler.createCommand returned null for handler={}, txn={}, assuming done for this handler",handler.getService(), data.getTransactionID());
            // Advance to next handler defensively
            processHandler(handler.next(), data);
            return;
        }
        CommandState cs = cmd.getCommandState();
        DataService ds = services.get(handler.getService());
        ExecutorService exec = executors.get(handler.getService());
        // INFO: Set the state, since the state in the data is used to extract the next command type, if not set would remain null
        if (ds == null) {       // Null data service
            log.error("No DataService found for '{}'. txn={}", handler.getService(), data.getTransactionID());
            data.setState(State.ERROR);
            // decide policy — stop, compensate, or skip. Here we stop on error.
            processHandler(handler, data);
            return;
        }
        if (exec == null) {     // Null executor
            log.error("No ExecutorService found for '{}'. txn={}", handler.getService(), data.getTransactionID());
            data.setState(State.ERROR);
            processHandler(handler, data);
            return;
        }
        log.info("Scheduling {} {} for txn={} on executor={}",handler.getService(), cs, data.getTransactionID(), exec);
        CompletableFuture
            .supplyAsync(() -> {        // Asynchronous call
                try {
                    return runServiceCommand(ds, cs, data);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                } catch (Throwable t) {
                    // rethrow after logging - the whenComplete will handle it
                    log.error("Exception executing service call. handler={}, cs={}, txn={}",handler.getService(), cs, data.getTransactionID(), t);
                    throw t;
                }
            }, exec)        // executor pool set, dynamically defined via map
            .whenComplete((state, ex) -> {
                if (ex != null) {
                    // surface exception details
                    log.error("Async task failed for handler={} cs={} txn={} : {}",
                            handler.getService(), cs, data.getTransactionID(), ex.toString(), ex);
                    // Decide a simple recovery: mark ERROR and let handler createCommand decide compensation
                    data.setState(State.ERROR);
                    processHandler(handler, data);
                    return;
                }
                // INFO: if the phase was EXTRACT, update the Data reference from the service's persisted view.
                if (cs == CommandState.EXTRACT) {
                    try {
                        Data extracted = ds.extract(data.getTransactionID());
                        if (extracted != null) {
                            // INFO: Here we mutate the caller's reference by copying values, we need to update data so we use the output and never discard it
                            copyDataFields(data, extracted);
                            dataStore.save(data);
                            log.debug("EXTRACT updated data for txn={}: {}", data.getTransactionID(), data);
                        } else {
                            log.warn("EXTRACT returned null for handler={} txn={}", handler.getService(), data.getTransactionID());
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log.error("Interrupted during EXTRACT for handler={} txn={}", handler.getService(), data.getTransactionID(), e);
                        data.setState(State.ERROR);
                        processHandler(handler, data);
                        return;
                    } catch (Throwable t) {
                        log.error("Error during EXTRACT handling for handler={} txn={}", handler.getService(), data.getTransactionID(), t);
                        data.setState(State.ERROR);
                        processHandler(handler, data);
                        return;
                    }
                }
                // set the logical state on the Data so handlers can inspect it
                data.setState(state);
                log.info("Completed {} {} for txn={}, result={}", handler.getService(), cs, data.getTransactionID(), state);
                // continuation logic
                if (state == State.PASS && cs == CommandState.EXTRACT) {        // INFO: When both pass and extract performed, that service is done for, move to next
                    processHandler(handler.next(), data);       // move to next service
                    return;
                }
                if (state == State.ERROR) {
                    processHandler(handler, data);
                    return;
                }
                if (state == State.RETRY) {
                    processHandler(handler, data);
                    return;
                }
                // PASS on RECEIVE/EXECUTE -> same handler next command
                processHandler(handler, data);
            });
    }

    private State runServiceCommand(DataService ds, CommandState cs, Data data) throws InterruptedException {
        // We deliberately call methods synchronously inside the worker thread.
        return switch (cs) {
            case RECEIVE -> ds.receive(data);
            case EXECUTE -> ds.execute(data);
            case COMPENSATE -> ds.compensate(data);
            case EXTRACT -> {
                // INFO: EXTRACT handled in whenComplete to accept the returned Data; here return PASS as placeholder
                yield ds.extract(data.getTransactionID()).getState();
            }
        };
    }

    // Naive shallow copy of data object, use visitor pattern when data is immutable
    private void copyDataFields(Data target, Data src) {
        if (src == null || target == null) return;
        target.setAadhar(src.getAadhar());
        target.setAge(src.getAge());
        target.setKycID(src.getKycID());
        target.setRiskScore(src.getRiskScore());
        target.setTokenID(src.getTokenID());
        target.setUser(src.getUser());
        target.setEvaluateRisk(src.isEvaluateRisk());
        // merge logs if present
        if (src.getLogs() != null) {
            if (target.getLogs() == null) target.setLogs(new ArrayList<>());
            target.getLogs().addAll(src.getLogs());
        }
        // copy other fields as needed
    }
}
