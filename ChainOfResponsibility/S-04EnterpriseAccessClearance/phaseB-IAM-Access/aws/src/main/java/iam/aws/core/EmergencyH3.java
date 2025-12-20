package iam.aws.core;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import iam.aws.data.output.Output;
import iam.aws.enums.Access;
import iam.aws.errors.AccessDenied;
import iam.aws.model.Handler;
import iam.aws.service.TokenManager;

@Component
public class EmergencyH3 implements Handler {
    private final String dBname = "E3";
    private final Executor executor;
    @Autowired private TokenManager tm;

    // technique: Constructor Injection - the annotated services are managed by Spring-boot lifecycle
    public EmergencyH3(@Qualifier("threads") Map<String, Executor> mp) {
        this.executor = mp.get(dBname);
    }

    @Override
    public CompletableFuture<Output> atomicExecution(Output data) {
        return CompletableFuture.completedFuture(data).thenApplyAsync(fn -> {
            // fixed: If Access is Denied, no need to go to the next Handler, can simply fallback
            String tokenID = fn.getTknData().getTokenID();
            if(tm.validateToken(tokenID))
                throw new AccessDenied("Token ID "+tokenID+" does not exist in the database");
            return fn;
        }, executor).exceptionally(ex -> {
            data.setAccess(Access.DENIED);
            // This creates a token when failed
            data.getTknData().setTokenID(tm.createToken(data.getTknData().getScopeID()));
            return data;
        });
    }
}
