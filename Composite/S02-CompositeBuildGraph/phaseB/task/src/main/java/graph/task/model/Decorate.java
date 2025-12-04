package graph.task.model;

import graph.task.data.context.ExecutionContext;

public interface Decorate {
    public ExecutionContext enrich(ExecutionContext exContext);
}
