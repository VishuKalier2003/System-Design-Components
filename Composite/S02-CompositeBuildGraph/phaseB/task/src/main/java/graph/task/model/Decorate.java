package graph.task.model;

import graph.task.data.context.ExecutionContext;

// technique: Functional interface to provide a single responsibility to a class (here decoration)
public interface Decorate {
    public ExecutionContext enrich(ExecutionContext exContext);
}
