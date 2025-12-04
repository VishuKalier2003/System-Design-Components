package graph.task.model.actions;

import graph.task.data.context.ExecutionContext;
import graph.task.data.input.ActionInput;

public interface IdentityAction {
    public ActionInput evaluate(ActionInput inp, ExecutionContext ctx);
}
