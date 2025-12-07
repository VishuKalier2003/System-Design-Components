package streaming.engine.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import streaming.engine.core.handler.ConcreteHandler;

@Component
public class HandlerStore {
    @Autowired private Factory factory;

    private final Map<String, ConcreteHandler> map = new HashMap<>();

    // fixed: creation should not use cache (it should just create objects according to Separation of Concerns principle)
    public ConcreteHandler createHandler(String name, String type) {
        ConcreteHandler handler = factory.createHandler(name, type);
        map.put(handler.getName(), handler);
        return handler;
    }

    public boolean exist(String name) {return map.containsKey(name);}

    // Can fire null value if wrong name given
    public ConcreteHandler get(String name) {return map.get(name);}
}
