package beyondcorp.google.core;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import beyondcorp.google.error.AccessFail;
import beyondcorp.google.error.EntryNotExistException;
import beyondcorp.google.model.Handler;
import beyondcorp.google.store.Output;
import beyondcorp.google.store.enums.DatabaseActions;
import beyondcorp.google.utils.Tokenizer;

@Component
@Order(6)
public class DatabaseH6 implements Handler {
    @Autowired private Tokenizer tokenizer;

    private Handler nextNode;

    @Override public CompletableFuture<Output> atomicOperation(Output input) {
        return CompletableFuture.completedFuture(input).thenApply(inp -> {
            String channel = inp.getTrustState().getData().get("channel");
            if(channel == null)
                throw new EntryNotExistException("channel");
            if(channel.length() < 5) {
                throw new AccessFail(DatabaseActions.UPDATE, "Database-Update-Handler");
            }
            inp.getInternalData().incrementPass();
            input.getInternalData().getLogs().add("PASS : Operation Access given for "+DatabaseActions.UPDATE);
            inp.getTokens().add(tokenizer.createToken(DatabaseActions.UPDATE, 180));
            return inp;
        }).exceptionally(fn -> {
            Throwable cause = fn.getCause();
            if(cause instanceof AccessFail) {
                input.getInternalData().getLogs().add("FAIL : Operation Access denied for "+DatabaseActions.UPDATE);
                input.getInternalData().incrementFail();
            } else if(cause instanceof EntryNotExistException) {
                input.getInternalData().getLogs().add("FAIL : Data does not exist for field channel");
                input.getInternalData().incrementFail();
            }
            return input;
        });
    }

    @Override public void next(Handler next) {this.nextNode = next;}
    @Override public Handler next() {return this.nextNode;}

    @Override public String getHandlerUuid() {return "Database-Update-Handler(H6)";}
}
