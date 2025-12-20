package iam.aws.core;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import iam.aws.data.output.Output;
import iam.aws.enums.Access;
import iam.aws.errors.AccessDenied;
import iam.aws.model.Handler;
import iam.aws.repo.ScopeRepo;

@Component
public class EmergencyH2 implements Handler {
    private final String dBname = "E2";
    private final Executor executor;
    private final ScopeRepo sr;

    // technique: Constructor Injection - the annotated services are managed by Spring-boot lifecycle
    public EmergencyH2(ScopeRepo eRepo, @Qualifier("threads") Map<String, Executor> mp) {
        this.sr = eRepo;
        this.executor = mp.get(dBname);
    }

    @Override
    public CompletableFuture<Output> atomicExecution(Output data) {
        return CompletableFuture.completedFuture(data).thenApplyAsync(fn -> {
            // fixed: If Access is Denied, no need to go to the next Handler, can simply fallback
            String scopeID = fn.getTknData().getScopeID();
            if(!sr.existsById(scopeID))
                throw new AccessDenied("Scope ID "+scopeID+" does not exist in the database");
            return fn;
        }, executor).exceptionally(ex -> {
            data.setAccess(Access.DENIED);
            return data;
        });
    }
}
