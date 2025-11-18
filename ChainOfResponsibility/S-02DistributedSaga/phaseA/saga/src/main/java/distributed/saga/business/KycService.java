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
public class KycService implements DataService {
    private final Map<String, Data> database = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(KycService.class);

    @Autowired private Serializer serializer;

    @Autowired private Database db;

    @Override public State receive(Data data) throws InterruptedException {
        log.info("Starting Kyc Service {} RECEIVE command",data.getTransactionID());
        db.insertService(data.getTransactionID(), "Kyc Service");
        db.insertCommand(data.getTransactionID(), CommandState.RECEIVE);
        Thread.sleep(2500);
        if(Math.random() < 0.25) {
            log.info("Failure at Kyc Service {} RECEIVE... retrying",data.getTransactionID());
            data.getLogs().add("Receive Status RETRY from Kyc Service...");
            return State.RETRY;
        }
        database.put(data.getTransactionID(), data);
        data.getLogs().add("Receive Status PASS from Kyc Service...");
        log.info("Completing Kyc Service {} RECEIVE command",data.getTransactionID());
        return State.PASS;
    }

    @Override public State execute(Data data) throws InterruptedException {
        log.info("Starting Kyc Service {} EXECUTE command",data.getTransactionID());
        db.insertService(data.getTransactionID(), "Kyc Service");
        db.insertCommand(data.getTransactionID(), CommandState.EXECUTE);
        Thread.sleep(8000);
        if(Integer.parseInt(data.getAadhar()) < 10000) {    // less than 5 digits
            log.info("Failure at Kyc Service {} EXECUTE command",data.getTransactionID());
            data.getLogs().add("Execute Status ERROR from Kyc Service, aadhar not correct...");
            return State.ERROR;
        }
        data.getLogs().add("Execute Status PASS from Kyc Service, generating a token ID...");
        database.get(data.getTransactionID()).setTokenID(serializer.generateKycID(data.getAadhar(), data.getTokenID()));
        log.info("Completing Kyc Service {} EXECUTE command",data.getTransactionID());
        return State.PASS;
    }

    @Override public State compensate(Data data) throws InterruptedException {
        log.info("Starting Kyc Service {} COMPENSATE command",data.getTransactionID());
        db.insertService(data.getTransactionID(), "Kyc Service");
        db.insertCommand(data.getTransactionID(), CommandState.COMPENSATE);
        Thread.sleep(8000);
        data.getLogs().add("Compensate Status PASS from Kyc Service, generating compensated token ID...");
        database.get(data.getTransactionID()).setKycID("COMPENSATED");
        log.info("Completing Kyc Service {} COMPENSATE command",data.getTransactionID());
        return State.PASS;
    }

    @Override public Data extract(String Id) throws InterruptedException {
        log.info("Completing Kyc Service {} EXTRACT command",Id);
        db.insertService(Id, "Kyc Service");
        db.insertCommand(Id, CommandState.EXTRACT);
        Thread.sleep(2000);
        database.get(Id).getLogs().add("Extract Status PASS from Kyc Service, moving to the next Handler");
        log.info("Completing Kyc Service {} COMPENSATE command",Id);
        return database.get(Id);
    }
}
