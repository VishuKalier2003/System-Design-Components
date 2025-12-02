package graph.composite.generator;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import graph.composite.core.Action;
import graph.composite.core.node.Container;
import graph.composite.core.node.Leaf;
import graph.composite.model.Composite;

@Component
public class CompositeFactory {
    // Object factory to return an object instance of type specified (managed by Spring)
    @Autowired private ObjectFactory<Action> actionFactory;

    private final String BASE = "Node-";
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public Composite createLeafNode(boolean leaf) {
        Action action = actionFactory.getObject();
        if(leaf) {
            Composite lf = new Leaf(BASE+idCounter.getAndIncrement());
            lf.setAction(action);
            return lf;
        }
        Composite ct = new Container(BASE+idCounter.getAndIncrement());
        ct.setAction(action);
        return ct;
    }
}
