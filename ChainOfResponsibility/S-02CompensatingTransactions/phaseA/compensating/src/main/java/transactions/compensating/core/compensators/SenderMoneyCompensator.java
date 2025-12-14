package transactions.compensating.core.compensators;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.Setter;
import transactions.compensating.data.input.Input;
import transactions.compensating.data.output.Output;
import transactions.compensating.database.Database;
import transactions.compensating.enums.Handlers;
import transactions.compensating.enums.ResourceRequest;
import transactions.compensating.enums.TransactionStatus;
import transactions.compensating.model.Compensator;
import transactions.compensating.model.Resource;

@Setter
@Component
// technique: synchronous compensators
public class SenderMoneyCompensator implements Compensator {
    @Autowired @Qualifier("pool") private Map<ResourceRequest, Resource> pool;

    private static final Handlers HANDLER = Handlers.SENDER_MONEY_COMPENSATOR;

    // The compensation is maintained synchronous and in order
    @Override public CompletableFuture<Output> atomicCompensation(Output output) {
        return CompletableFuture.completedFuture(output).thenApply(fn -> {
            Resource res = pool.get(ResourceRequest.DATABASE);
            if(res instanceof Database db) {
                Input ip = output.getInput();
                String hash = ip.getTransferFrom().getUsername() + "-" + ip.getTransferFrom().getBank();
                db.setAmount(hash, +ip.getAmount());
            }
            Output.Pair pair = output.new Pair(HANDLER, TransactionStatus.COMPENSATE);
            output.getActions().add(pair);
            output.getLogs().add("Compensation performed : Sender received money back");
            return fn;
        });
    }
}
