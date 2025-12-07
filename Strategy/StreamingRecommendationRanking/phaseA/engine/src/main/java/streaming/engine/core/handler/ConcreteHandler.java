package streaming.engine.core.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import streaming.engine.data.input.Request;
import streaming.engine.data.output.Output;
import streaming.engine.enums.data.RailType;
import streaming.engine.error.EmptyStrategyException;
import streaming.engine.model.Handler;
import streaming.engine.model.Strategy;

// technique: layer decoupling, the handler layer does not uses any database service (database/store/etc.) directly
@Component
@Scope("prototype")
public class ConcreteHandler implements Handler {
    // layer coupling allows the different layers to scale differently without directly manipulating other layers
    private Strategy currentStrategy;

    private final ReentrantLock lock = new ReentrantLock();
    private String NAME;
    private RailType currentRailType;

    public void setName(String name) {this.NAME = name;}
    public void setRailType(String name) {this.currentRailType = RailType.valueOf(name.toUpperCase());}
    public String getName() {return this.NAME;}

    @Override public List<Output> executeStrategy(Request request) {
        try {
            lock.lock();
            if(currentStrategy == null)
                throw new EmptyStrategyException(NAME);
            return currentStrategy.strategy().apply(request, currentRailType);
        }
        catch(EmptyStrategyException e) {
            System.out.println("NPE : passing empty list as output");
            return new ArrayList<>();
        }
        finally {
            lock.unlock();
        }
    }

    @Override public void setCurrentStrategy(Strategy strategy) {this.currentStrategy = strategy;}

    // fixed: always ensure that the setters are correctly defined
    @Override public Strategy getCurrentStrategy() {return this.currentStrategy;}
    @Override public RailType getCurrentRailType() {return this.currentRailType;}
}
