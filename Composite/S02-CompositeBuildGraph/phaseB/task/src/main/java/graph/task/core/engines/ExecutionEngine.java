package graph.task.core.engines;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Component;

import graph.task.data.context.ExecutionContext;
import graph.task.data.input.ActionInput;
import graph.task.data.output.ActionOutput;
import graph.task.enums.ActionStatus;
import graph.task.enums.func.BaseFunction;
import graph.task.model.actions.Action;
import graph.task.model.modules.Executable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
public class ExecutionEngine implements Action {
    private Executable engineFunction;
    private final ReentrantLock lock = new ReentrantLock();

    public ExecutionEngine() {
        engineFunction = BaseFunction.BASE_METAL_PURE;
    }

    @Override public ActionOutput execute(ActionInput inp, ExecutionContext ec) {
        try {
            lock.lock();
            return engineFunction.swappableFunction().apply(inp, ec);
        } catch(Exception e) {
            List<String> lst = new ArrayList<>();
            lst.add("ERROR IN BUILDING");
            return ActionOutput.builder().actionStatus(ActionStatus.FAIL).logs(lst).build();
        } finally {
            lock.unlock();
        }
    }
}
