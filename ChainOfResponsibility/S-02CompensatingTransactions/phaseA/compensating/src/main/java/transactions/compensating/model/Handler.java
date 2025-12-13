package transactions.compensating.model;

import java.util.concurrent.CompletableFuture;

import transactions.compensating.data.output.Output;
import transactions.compensating.enums.Handlers;

public interface Handler {
    public CompletableFuture<Output> atomicExecution(Output output);

    public void next(Handler handler);

    public Handler next();

    public Handlers activateCompensator();
}
