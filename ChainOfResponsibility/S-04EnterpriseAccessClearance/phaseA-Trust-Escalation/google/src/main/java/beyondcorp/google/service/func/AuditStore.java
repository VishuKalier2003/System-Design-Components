package beyondcorp.google.service.func;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import beyondcorp.google.store.Output;

@Component
public class AuditStore {
    // The string will be transactionID not uuid
    private final Map<String, Output.ChainData> store = new HashMap<>();
    private final Map<String, List<String>> logStore = new HashMap<>();

    public void pushAuditLogs(Output.ChainData data) {
        logStore.put(data.getTransactionID(), data.getLogs());
    }

    public Map<String, List<String>> viewLogs() {return logStore;}

    public boolean pushAudit(Output.ChainData data) {
        store.put(data.getTransactionID(), data);
        return true;
    }

    public Output.ChainData get(String key) {
        return store.get(key);
    }

    public boolean clearEntry(String key) {
        if(store.containsKey(key)) {
            store.remove(key);
        }
        return true;
    }

    // All the Audits can be viewed only by the admin
    public Map<String, Output.ChainData> getAll() {
        return store;
    }
}
