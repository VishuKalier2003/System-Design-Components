package graph.composite.model;

import java.util.function.Function;

import graph.composite.dto.ExecutionOutput;
import graph.composite.utils.ConfigBlock;

public interface ExecuteAction {
    public Function<ConfigBlock, ExecutionOutput> executionFunction();
}
