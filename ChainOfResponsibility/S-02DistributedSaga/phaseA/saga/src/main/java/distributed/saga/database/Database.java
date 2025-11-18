package distributed.saga.database;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import distributed.saga.data.CommandState;
import distributed.saga.input.Output;

@Component
public class Database {
    private final Map<String, String> serviceMap = new HashMap<>();
    private final Map<String, CommandState> commandMap = new HashMap<>();

    public void insertService(String key, String value) {this.serviceMap.put(key, value);}

    public void insertCommand(String key, CommandState value) {this.commandMap.put(key, value);}

    public Output show(String key) {
        Output output = new Output();
        output.setTransactionID(key);
        output.setCurrentService(serviceMap.get(key));
        output.setCurrentCommand(commandMap.get(key));
        return output;
    }
}
