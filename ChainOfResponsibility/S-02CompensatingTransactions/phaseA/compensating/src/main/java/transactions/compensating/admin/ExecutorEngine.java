package transactions.compensating.admin;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.Setter;
import transactions.compensating.data.User;
import transactions.compensating.data.input.Input;
import transactions.compensating.data.output.Output;
import transactions.compensating.database.Database;
import transactions.compensating.enums.Handlers;
import transactions.compensating.model.Compensator;
import transactions.compensating.model.Handler;
import transactions.compensating.utils.KeyGenerator;

@Setter
@Service
public class ExecutorEngine {

    @Autowired
    private ChainManager cm;
    @Autowired
    @Qualifier("compensators")
    private Map<Handlers, Compensator> compensatorRegistry;
    @Autowired
    private KeyGenerator generator;
    @Autowired
    private Database db;

    private static final int ALLOWED = 3;

    public Output executeChain(Input input) throws Exception {
        String txId = generator.createKey();
        Deque<Compensator> stack = new ArrayDeque<>();
        Output output = Output.builder()
                .actions(new ArrayList<>())
                .logs(new ArrayList<>())
                .transactionID(txId)
                .failed(false)
                .retry(0)
                .input(input)
                .build();
        Handler current = cm.getHead();
        while (current != null) {
            output = current.atomicExecution(output).get();
            if (output.isFailed()) {
                break;
            }
            // RETRY handling
            if (output.lastActionIsRetry()) {
                output.incrementRetry();
                if (output.getRetry() > ALLOWED) {
                    output.setFailed(true);
                    break;
                }
                // Retry SAME handler again
                continue;
            }
            // Register compensator ONLY on success
            Handlers compId = current.activateCompensator();
            if (compId != null) {
                stack.addLast(compensatorRegistry.get(compId));
            }
            current = current.next();
        }
        return output;
    }

    public void register(User user) {
        db.register(user);
    }

}
