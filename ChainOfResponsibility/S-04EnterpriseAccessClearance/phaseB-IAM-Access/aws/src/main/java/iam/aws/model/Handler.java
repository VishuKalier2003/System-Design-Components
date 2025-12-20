package iam.aws.model;

import java.util.concurrent.CompletableFuture;

import iam.aws.data.output.Output;

public interface Handler {
    public CompletableFuture<Output> atomicExecution(Output output);
}
