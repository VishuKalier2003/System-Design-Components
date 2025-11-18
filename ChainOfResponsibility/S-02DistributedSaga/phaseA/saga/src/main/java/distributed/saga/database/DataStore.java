package distributed.saga.database;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import distributed.saga.data.Data;

@Component
public class DataStore {
    private final Map<String, Data> dataMap = new ConcurrentHashMap<>();

    public void save(Data data) {
        dataMap.put(data.getTransactionID(), data);
    }

    public Data get(String txnId) {
        return dataMap.get(txnId);
    }
}
