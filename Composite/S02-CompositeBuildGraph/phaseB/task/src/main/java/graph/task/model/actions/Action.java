package graph.task.model.actions;

import graph.task.data.context.ExecutionContext;
import graph.task.data.input.ActionInput;
import graph.task.data.output.ActionOutput;

// Used by Engine for layering the abstraction
public interface Action {
    public ActionOutput execute(ActionInput inp, ExecutionContext ec);
}
