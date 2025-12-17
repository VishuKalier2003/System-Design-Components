package beyondcorp.google.core;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import beyondcorp.google.error.AccessFail;
import beyondcorp.google.error.EntryNotExistException;
import beyondcorp.google.model.Handler;
import beyondcorp.google.store.Output;
import beyondcorp.google.store.enums.AuditActions;
import beyondcorp.google.utils.Tokenizer;

@Component
@Order(3)
public class AuditH3 implements Handler {
    @Autowired private Tokenizer tokenizer;

    private Handler nextNode;

    @Override public CompletableFuture<Output> atomicOperation(Output input) {
        return CompletableFuture.completedFuture(input).thenApply(inp -> {
            String geo = inp.getTrustState().getData().get("geo");
            if(geo == null)
                throw new EntryNotExistException("geo");
            if(!geo.equalsIgnoreCase("INDIA"))
                throw new AccessFail(AuditActions.PUSH,"Audit-Push-handler");
            inp.getInternalData().incrementPass();
            input.getInternalData().getLogs().add("PASS : Operation Access passed for "+AuditActions.PUSH);
            inp.getTokens().add(tokenizer.createToken(AuditActions.PUSH, 60));
            return inp;
        }).exceptionally(fn -> {
            Throwable cause = fn.getCause();
            if(cause instanceof AccessFail) {
                input.getInternalData().getLogs().add("FAIL : Operation Access denied for "+AuditActions.PUSH);
                input.getInternalData().incrementFail();
            } else if(cause instanceof EntryNotExistException) {
                input.getInternalData().getLogs().add("FAIL : Data does not exist for field geo");
                input.getInternalData().incrementFail();
            }
            return input;
        });
    }

    @Override public void next(Handler next) {this.nextNode = next;}
    @Override public Handler next() {return this.nextNode;}

    @Override public String getHandlerUuid() {return "Audit-Push-Handler(H3)";}
}
