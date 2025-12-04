package graph.task.model.modules;

import java.util.function.BiFunction;

import graph.task.data.context.ExecutionContext;
import graph.task.data.input.ActionInput;

public interface Preprocessing {
    public BiFunction<ActionInput, ExecutionContext, ActionInput> preprocessFunction();
}
