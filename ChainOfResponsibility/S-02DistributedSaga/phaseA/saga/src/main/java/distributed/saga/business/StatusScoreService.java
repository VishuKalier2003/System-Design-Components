package distributed.saga.business;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import distributed.saga.data.CommandState;
import distributed.saga.data.Data;
import distributed.saga.data.State;
import distributed.saga.database.Database;
import distributed.saga.model.DataService;
import distributed.saga.utils.Serializer;

@Service
public class StatusScoreService implements DataService {
    private final Map<String, Data> database = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(StatusScoreService.class);

    @Autowired private Serializer serializer;
    @Autowired private Database db;

    @Override public State receive(Data data) throws InterruptedException {
        log.info("Starting Status Score Service {} RECEIVE command",data.getTransactionID());
        db.insertService(data.getTransactionID(), "Status Score Service");
        db.insertCommand(data.getTransactionID(), CommandState.RECEIVE);
        Thread.sleep(2500);
        if(Math.random() < 0.25) {
            log.info("Failure at Status Score Service {} RECEIVE... retrying",data.getTransactionID());
            data.getLogs().add("Receive Status RETRY from Status Score Service...");
            return State.RETRY;
        }
        database.put(data.getTransactionID(), data);
        data.getLogs().add("Receive Status PASS from Status Score Service...");
        log.info("Completing Status Score Service {} RECEIVE command",data.getTransactionID());
        return State.PASS;
    }

    @Override public State execute(Data data) throws InterruptedException {
        System.out.println("Starting Status Score Service EXECUTE");
        db.insertService(data.getTransactionID(), "Status Score Service");
        db.insertCommand(data.getTransactionID(), CommandState.EXECUTE);
        Thread.sleep(5000);
        if(!data.isEvaluateRisk()) {
            log.info("Failure at Status Score Service {} EXECUTE command",data.getTransactionID());
            data.getLogs().add("Execute Status ERROR from Status Score Service, evaluate risk not wanted...");
            return State.ERROR;
        }
        data.getLogs().add("Execute Status PASS from Status Score Service, generating risk Score...");
        database.get(data.getTransactionID()).setRiskScore(serializer.evaluateRisk(data.getKycID(), data.getTokenID()));
        log.info("Completing Status Score Service {} EXECUTE command",data.getTransactionID());
        return State.PASS;
    }

    @Override public State compensate(Data data) throws InterruptedException {
        log.info("Starting Status Score Service {} COMPENSATE command",data.getTransactionID());
        db.insertService(data.getTransactionID(), "Status Score Service");
        db.insertCommand(data.getTransactionID(), CommandState.COMPENSATE);
        Thread.sleep(5000);
        data.getLogs().add("Compensate Status PASS from Status Score Service, generating compensated risk Score...");
        database.get(data.getTransactionID()).setRiskScore(22/7.0d);
        log.info("Completing Status Score Service {} COMPENSATE command",data.getTransactionID());
        return State.PASS;
    }

    @Override public Data extract(String Id) throws InterruptedException {
        log.info("Completing Status Score Service {} EXTRACT command",Id);
        db.insertService(Id, "Status Score Service");
        db.insertCommand(Id, CommandState.EXTRACT);
        Thread.sleep(2000);
        database.get(Id).getLogs().add("Extract Status PASS from Status Score Service, moving to the next Handler");
        log.info("Completing Status Score Service {} COMPENSATE command",Id);
        return database.get(Id);
    }
}
