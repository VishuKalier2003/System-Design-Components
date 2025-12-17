package beyondcorp.google.core;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import beyondcorp.google.database.Database;
import beyondcorp.google.error.AccessFail;
import beyondcorp.google.model.Handler;
import beyondcorp.google.store.Output;
import beyondcorp.google.store.enums.DatabaseActions;
import beyondcorp.google.utils.Tokenizer;

@Component
@Order(1)
public class DatabaseH1 implements Handler {
    @Autowired private Database db;
    @Autowired private Tokenizer tokenizer;

    private Handler nextNode;

    @Override public CompletableFuture<Output> atomicOperation(Output input) {
        return CompletableFuture.completedFuture(input).thenApply(inp -> {
            if(!db.ifExist(input.getTrustState().getUuid())) {
                throw new AccessFail(DatabaseActions.GET, "Database-Get-Handler");
            }
            inp.getInternalData().incrementPass();
            input.getInternalData().getLogs().add("PASS : Operation Access given for "+DatabaseActions.GET);
            inp.getTokens().add(tokenizer.createToken(DatabaseActions.GET, 180));
            return inp;
        }).exceptionally(fn -> {
            Throwable cause = fn.getCause();
            if(cause instanceof AccessFail) {
                input.getInternalData().getLogs().add("FAIL : Operation Access denied for "+DatabaseActions.GET);
                input.getInternalData().incrementFail();
            }
            return input;
        });
    }

    @Override public void next(Handler next) {this.nextNode = next;}
    @Override public Handler next() {return this.nextNode;}

    @Override public String getHandlerUuid() {return "Database-Get-Handler(H1)";}
}
