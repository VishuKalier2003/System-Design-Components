package beyondcorp.google.admin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import beyondcorp.google.model.Handler;
import beyondcorp.google.service.func.AuditStore;
import beyondcorp.google.store.Output;

@Service
public class ChainEngine {
    @Autowired private ChainManager manager;
    @Autowired private AuditStore auditStore;
    @Autowired private TrustEngine trustEngine;

    public Output executeRequest(Output input) {
        CompletableFuture<Output> f;
        Handler currentNode = manager.getHead();
        try {
            while(currentNode != null) {
                f = currentNode.atomicOperation(input);
                input = f.get(10, TimeUnit.SECONDS);
                input.getInternalData().setPerformance(trustEngine.evaluateTrustPerformance(input.getInternalData()));
                input.getTrustState().setLevel(trustEngine.getCurrentTrustLevel(input.getInternalData()));
                currentNode = currentNode.next();
            }
        } catch(ExecutionException | InterruptedException | TimeoutException e) {
                Thread.currentThread().interrupt();
        }
        Output.ChainData data = input.getInternalData();
        auditStore.pushAuditLogs(data);
        // technique: log-hiding from user, the logs are sent to the audit store and then deleted from user response
        input.set();
        return input;
    }
}
