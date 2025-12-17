package beyondcorp.google.core;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import beyondcorp.google.error.AccessFail;
import beyondcorp.google.model.Handler;
import beyondcorp.google.store.Output;
import beyondcorp.google.store.enums.AuditActions;
import beyondcorp.google.utils.Tokenizer;

@Component
@Order(4)
public class AuditH4 implements Handler {
    @Autowired private Tokenizer tokenizer;

    private Handler nextNode;

    @Override public CompletableFuture<Output> atomicOperation(Output input) {
        return CompletableFuture.completedFuture(input).thenApply(inp -> {
            if(Math.random() < 0.25)
                throw new AccessFail(AuditActions.GET, "Audit-Get-handler");
            inp.getInternalData().incrementPass();
            input.getInternalData().getLogs().add("PASS : Operation Access passed for "+AuditActions.GET);
            inp.getTokens().add(tokenizer.createToken(AuditActions.GET, 60));
            return inp;
        }).exceptionally(fn -> {
            Throwable cause = fn.getCause();
            if(cause instanceof AccessFail) {
                input.getInternalData().getLogs().add("FAIL : Operation Access denied for "+AuditActions.GET);
                input.getInternalData().incrementFail();
            }
            return input;
        });
    }

    @Override public void next(Handler next) {this.nextNode = next;}
    @Override public Handler next() {return this.nextNode;}

    @Override public String getHandlerUuid() {return "Audit-Get-Handler(H4)";}
}
