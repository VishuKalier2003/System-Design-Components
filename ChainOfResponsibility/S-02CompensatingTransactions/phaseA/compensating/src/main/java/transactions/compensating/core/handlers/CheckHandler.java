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
import transactions.compensating.data.input.Input;
import transactions.compensating.data.output.Output;
import transactions.compensating.database.Database;
import transactions.compensating.enums.Handlers;
import transactions.compensating.enums.ResourceRequest;
import transactions.compensating.enums.TransactionStatus;
import transactions.compensating.error.NotInDatabaseException;
import transactions.compensating.model.Handler;
import transactions.compensating.model.Resource;

@Setter
@Component
public class CheckHandler implements Handler {
    @Autowired @Qualifier("pool") private Map<ResourceRequest, Resource> pool;
    @Autowired @Qualifier("threads") private Map<Handlers, ExecutorService> threads;

    // technique: The Executor is property of each handler so we do not pass it as function parameter (data hiding)
    private Executor executor;
    private Handler next;

    private static final Handlers HANDLER = Handlers.CHECK;

    @PostConstruct
    public void init() {
        this.executor = threads.get(HANDLER);
    }

    @Override
    public CompletableFuture<Output> atomicExecution(Output output) {
        return CompletableFuture.completedFuture(output).thenApplyAsync(x -> {
            if (output.isFailed())
                return output;
            Resource resource = pool.get(ResourceRequest.DATABASE);
            if (resource instanceof Database db) {
                Input inp = output.getInput();
                if (!db.contains(inp.getTransferTo()))
                    throw new NotInDatabaseException(inp.getTransferTo().getUsername(), "RECEIVER");
                else if (!db.contains(inp.getTransferFrom()))
                    throw new NotInDatabaseException(inp.getTransferFrom().getUsername(), "SENDER");
            }
            return output;
        }, executor).exceptionally(fn -> {
            Throwable cause = fn.getCause();
            if (cause instanceof NotInDatabaseException) {
                output.setFailed(true);
                Output.Pair p = output.new Pair(HANDLER, TransactionStatus.FAIL);
                output.getActions().add(p);
                output.getLogs().add("Database entry does not exist");
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
