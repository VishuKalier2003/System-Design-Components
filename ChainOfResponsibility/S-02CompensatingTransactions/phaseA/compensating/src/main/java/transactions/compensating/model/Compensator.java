package transactions.compensating.model;

import java.util.concurrent.CompletableFuture;

import transactions.compensating.data.output.Output;

public interface Compensator {
    public CompletableFuture<Output> atomicCompensation(Output output);
}
