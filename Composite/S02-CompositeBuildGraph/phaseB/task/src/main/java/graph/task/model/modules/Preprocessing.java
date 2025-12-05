package graph.task.model.modules;

import java.util.function.BiFunction;

import graph.task.data.context.ExecutionContext;
import graph.task.data.input.ActionInput;
import graph.task.model.Marker;

// technique: domain expansion for interface pertaining to Domain Driven Design (DDD)
public interface Preprocessing extends Marker {
    // explicit function can be used when down-casted from Marker to Executable
    public BiFunction<ActionInput, ExecutionContext, ActionInput> preprocessFunction();
}
