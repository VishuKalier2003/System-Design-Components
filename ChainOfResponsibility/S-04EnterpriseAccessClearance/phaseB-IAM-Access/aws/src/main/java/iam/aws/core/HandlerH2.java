package iam.aws.core;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Qualifier;

import iam.aws.data.output.Output;
import iam.aws.enums.Access;
import iam.aws.errors.AccessDenied;
import iam.aws.model.Handler;

public class HandlerH2 implements Handler {
    private final String dBname = "H3";
    private final Executor executor;

    public HandlerH2(@Qualifier("threads") Map<String, Executor> mp) {
        this.executor = mp.get(dBname);
    }

    @Override
    public CompletableFuture<Output> atomicExecution(Output data) {
        return CompletableFuture.completedFuture(data).thenApplyAsync(fn -> {
            String s = data.getInfo().getReceiver();
            if(!s.equals(data.getInfo().getRequester()))
                throw new AccessDenied("The one and only receiver should be the sender only");
            return fn;
        }, executor).exceptionally(ex -> {
            data.setAccess(Access.DENIED);
            return data;
        });
    }
}
