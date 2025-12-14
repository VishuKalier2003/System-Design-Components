package transactions.compensating.data.output;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import transactions.compensating.data.input.Input;
import transactions.compensating.enums.Handlers;
import transactions.compensating.enums.TransactionStatus;

@Getter
@Setter
@Builder(toBuilder = true)
public class Output { // use builder to create objects
    private List<String> logs;
    private List<Pair> actions;
    private Input input;
    private boolean failed;
    private int retry;
    private String transactionID;

    @Getter
    @AllArgsConstructor
    public class Pair {
        private final Handlers handlerID;
        private final TransactionStatus status;
    }

    public boolean lastActionIsRetry() {
        if (actions.isEmpty())
            return false;
        return actions.get(actions.size() - 1).getStatus() == TransactionStatus.RETRY;
    }

    public void incrementRetry() {
        this.retry++;
    }

    public void show() {
        int size = actions.size();
        System.out.println("Transaction ID : "+transactionID);
        System.out.println("Failed ? "+failed);
        System.out.println("Retries : "+retry);
        for(int i = 0; i < size; i++)
            System.out.println("Handler ID : "+actions.get(i).handlerID+" Status : "+actions.get(i).status+" Log : "+logs.get(i));
    }
}
