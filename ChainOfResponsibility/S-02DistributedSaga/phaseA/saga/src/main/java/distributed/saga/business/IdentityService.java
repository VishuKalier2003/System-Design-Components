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
public class IdentityService implements DataService {
    private static final Logger log = LoggerFactory.getLogger(IdentityService.class);

    private final Map<String, Data> database = new HashMap<>();

    @Autowired private Serializer serializer;
    @Autowired private Database db;

    @Override public State receive(Data data) throws InterruptedException {
        log.info("Starting Identity Service {} RECEIVE command",data.getTransactionID());
        db.insertService(data.getTransactionID(), "Identity Service");
        db.insertCommand(data.getTransactionID(), CommandState.RECEIVE);
        Thread.sleep(2000);
        if(Math.random() < 0.25) {
            log.info("Failure at Identity Service {} RECEIVE... retrying",data.getTransactionID());
            data.getLogs().add("Receive Status RETRY from Identity Service...");
            return State.RETRY;
        }
        database.put(data.getTransactionID(), data);
        data.getLogs().add("Receive Status PASS from Identity Service...");
        log.info("Completing Identity Service {} RECEIVE command",data.getTransactionID());
        return State.PASS;
    }

    @Override public State execute(Data data) throws InterruptedException {
        log.info("Starting Identity Service {} EXECUTE command",data.getTransactionID());
        db.insertService(data.getTransactionID(), "Identity Service");
        db.insertCommand(data.getTransactionID(), CommandState.EXECUTE);
        Thread.sleep(5000);
        if(Character.isLowerCase(data.getUser().charAt(0)) || data.getAge() < 18) {
            log.info("Failure at Identity Service {} EXECUTE command",data.getTransactionID());
            data.getLogs().add("Execute Status ERROR from Identity Service, username not in camelcase or age quite low...");
            return State.ERROR;
        }
        data.getLogs().add("Execute Status PASS from Identity Service, generating a token ID...");
        database.get(data.getTransactionID()).setTokenID(serializer.generateTokenID(data.getUser(), data.getAge()));
        log.info("Completing Identity Service {} EXECUTE command",data.getTransactionID());
        return State.PASS;
    }

    @Override public State compensate(Data data) throws InterruptedException {
        log.info("Starting Identity Service {} COMPENSATE command",data.getTransactionID());
        db.insertService(data.getTransactionID(), "Identity Service");
        db.insertCommand(data.getTransactionID(), CommandState.COMPENSATE);
        Thread.sleep(8000);
        data.getLogs().add("Compensate Status PASS from Identity Service, generating compensated token ID...");
        database.get(data.getTransactionID()).setTokenID("COMPENSATED");
        log.info("Completing Identity Service {} COMPENSATE command",data.getTransactionID());
        return State.PASS;
    }

    @Override public Data extract(String Id) throws InterruptedException {
        log.info("Completing Identity Service {} EXTRACT command",Id);
        db.insertService(Id, "Identity Service");
        db.insertCommand(Id, CommandState.EXTRACT);
        Thread.sleep(2000);
        database.get(Id).getLogs().add("Extract Status PASS from Identity Service, moving to the next Handler");
        log.info("Completing Identity Service {} COMPENSATE command",Id);
        return database.get(Id);
    }
}
