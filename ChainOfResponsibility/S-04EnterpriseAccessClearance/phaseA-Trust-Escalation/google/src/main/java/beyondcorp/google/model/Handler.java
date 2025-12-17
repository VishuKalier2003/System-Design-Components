package beyondcorp.google.model;

import java.util.concurrent.CompletableFuture;

import beyondcorp.google.store.Output;

public interface Handler {
    public CompletableFuture<Output> atomicOperation(Output input);
    public void next(Handler nextNode);
    public Handler next();
    public String getHandlerUuid();
}
