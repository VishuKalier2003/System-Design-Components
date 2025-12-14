package transactions.compensating.core.handlers;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.Setter;
import transactions.compensating.data.output.Output;
import transactions.compensating.enums.Handlers;
import transactions.compensating.enums.ResourceRequest;
import transactions.compensating.enums.TransactionStatus;
import transactions.compensating.error.Escalation;
import transactions.compensating.model.Handler;
import transactions.compensating.model.Resource;
import transactions.compensating.service.TransactionQuotas;

@Setter
@Component
public class QuotaHandler implements Handler {
    @Autowired @Qualifier("pool") private Map<ResourceRequest, Resource> pool;
    @Autowired @Qualifier("threads") private Map<Handlers, ExecutorService> threads;

    // technique: The Executor is property of each handler so we do not pass it as function parameter (data hiding)
    private Executor executor;
    private Handler next;

    private static final int ALLOWED = 3;
    private static final Handlers HANDLER = Handlers.QUOTA;

    @PostConstruct
    public void init() {
        this.executor = threads.get(HANDLER);
    }

    @Override
    public CompletableFuture<Output> atomicExecution(Output output) {
        return CompletableFuture.completedFuture(output).thenApplyAsync(x -> {
            if (output.isFailed())
                return output;
            if(output.getRetry() >= ALLOWED) {
                throw new Escalation(HANDLER.toString());
            }
            Resource resource = pool.get(ResourceRequest.QUOTAS);
            if(resource instanceof TransactionQuotas q) {
                if(!q.tokensExist()) {
                    Output.Pair p = output.new Pair(HANDLER, TransactionStatus.RETRY);
                    output.getActions().add(p);
                    output.getLogs().add("Retrying for Quota access");
                    return output;
                }
                else
                    output.getLogs().add("Grabbed token ID "+q.catchToken());
            }
            Output.Pair p = output.new Pair(HANDLER, TransactionStatus.PASS);
            output.getActions().add(p);
            return output;
        }, executor).exceptionally(fn -> {
            Throwable cause = fn.getCause();
            if (cause instanceof Escalation) {
                output.setFailed(true);
                Output.Pair p = output.new Pair(HANDLER, TransactionStatus.ESCALATE);
                output.getActions().add(p);
                output.getLogs().add("Escalated...");
            }
            return output;
        });
    }

    @Override public void next(Handler handler) {this.next = handler;}
    @Override public Handler next() {return this.next;}

    @Override public Handlers activateCompensator() {
        return null;
    }
}
