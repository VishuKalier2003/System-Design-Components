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
@Order(2)
public class DatabaseH2 implements Handler {
    @Autowired private Tokenizer tokenizer;

    private Handler nextNode;

    @Override public CompletableFuture<Output> atomicOperation(Output input) {
        return CompletableFuture.completedFuture(input).thenApply(inp -> {
            String name = inp.getTrustState().getData().get("name");
            if(name == null)
                throw new EntryNotExistException("name");
            boolean upper = false, lower = false;
            for(char ch : name.toCharArray()) {
                if(Character.isLowerCase(ch))
                    lower = true;
                else if(Character.isUpperCase(ch))
                    upper = true;
                if(lower && upper)
                    break;
            }
            if(!lower || !upper) {
                throw new AccessFail(DatabaseActions.CREATE, "Database-Create-handler");
            }
            inp.getInternalData().incrementPass();
            input.getInternalData().getLogs().add("PASS : Operation Access given for "+DatabaseActions.CREATE);
            inp.getTokens().add(tokenizer.createToken(DatabaseActions.CREATE, 120));
            return inp;
        }).exceptionally(fn -> {
            Throwable cause = fn.getCause();
            if(cause instanceof AccessFail) {
                input.getInternalData().getLogs().add("FAIL : Operation Access denied for "+DatabaseActions.CREATE);
                input.getInternalData().incrementFail();
            } else if(cause instanceof EntryNotExistException) {
                input.getInternalData().getLogs().add("FAIL : Data does not exist for field name");
                input.getInternalData().incrementFail();
            }
            return input;
        });
    }

    @Override public void next(Handler next) {this.nextNode = next;}
    @Override public Handler next() {return this.nextNode;}

    @Override public String getHandlerUuid() {return "Database-Create-Handler(H2)";}
}
