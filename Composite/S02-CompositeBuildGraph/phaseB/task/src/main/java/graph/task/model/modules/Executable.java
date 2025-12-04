package graph.task.model.modules;

import java.util.function.BiFunction;

import graph.task.data.context.ExecutionContext;
import graph.task.data.input.ActionInput;
import graph.task.data.output.ActionOutput;

public interface Executable {
    public BiFunction<ActionInput, ExecutionContext, ActionOutput> swappableFunction();
}
