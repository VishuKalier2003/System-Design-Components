package graph.task.core.engines;

import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Component;

import graph.task.data.context.ExecutionContext;
import graph.task.data.input.ActionInput;
import graph.task.enums.func.RetryFunction;
import graph.task.model.actions.IdentityAction;
import graph.task.model.modules.Preprocessing;
import lombok.Setter;

// technique: separate layer pluggable Engine - stores the operations of Engine and never the data
@Setter
@Component
public class RectificationEngine implements IdentityAction {
    private Preprocessing preprocessFunction;       // pluggable interface
    private final ReentrantLock lock;

    public RectificationEngine() {
        preprocessFunction = RetryFunction.QUARTER;
        lock = new ReentrantLock();
    }

    @Override public ActionInput evaluate(ActionInput inp, ExecutionContext ctx) {
        try {
            lock.lock();
            return preprocessFunction.preprocessFunction().apply(inp, ctx);
        } catch(Exception e) {
            System.out.println("ERROR DURING PREPROCESSING");
            return null;
        } finally {
            lock.unlock();
        }
    }
}
