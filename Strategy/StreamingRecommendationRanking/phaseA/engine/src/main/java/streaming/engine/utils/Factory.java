package streaming.engine.utils;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import streaming.engine.core.handler.ConcreteHandler;

@Component
public class Factory {
    @Autowired private ObjectFactory<ConcreteHandler> factory;

    public ConcreteHandler createHandler(String name, String railType) {
        // detail: ensure that the defined class has an explicit no-args constructor
        ConcreteHandler handler = factory.getObject();
        handler.setName(name);
        handler.setRailType(railType);
        return handler;
    }
}
