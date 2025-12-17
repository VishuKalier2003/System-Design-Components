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
@Order(7)
public class AuditH7 implements Handler {
    @Autowired private Tokenizer tokenizer;

    private Handler nextNode;

    @Override public CompletableFuture<Output> atomicOperation(Output input) {
        return CompletableFuture.completedFuture(input).thenApply(inp -> {
            String uuid = inp.getTrustState().getUuid();
            int cUpper = 0, cLower = 0;
            for(char c : uuid.toCharArray()) {
                if(Character.isLowerCase(c))
                    cLower++;
                else if(Character.isUpperCase(c))
                    cUpper++;
            }
            if(cLower < 4 && cUpper < 4)
                throw new AccessFail(AuditActions.ALL, "Audit-All-handler");
            inp.getInternalData().incrementPass();
            input.getInternalData().getLogs().add("PASS : Operation Access given for "+AuditActions.ALL);
            inp.getTokens().add(tokenizer.createToken(AuditActions.ALL, 60));
            return inp;
        }).exceptionally(fn -> {
            Throwable cause = fn.getCause();
            if(cause instanceof AccessFail) {
                input.getInternalData().getLogs().add("FAIL : Operation Access denied for "+AuditActions.ALL);
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

    @Override public String getHandlerUuid() {return "Audit-All-Handler(H7)";}
}
