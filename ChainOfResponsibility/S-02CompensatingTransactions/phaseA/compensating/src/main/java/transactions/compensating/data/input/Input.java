package transactions.compensating.data.input;

import lombok.Getter;
import lombok.Setter;
import transactions.compensating.data.User;

@Getter
@Setter
// The manager will generate a transactionID using a service
public class Input {
    private String transactionID;
    private int amount;
    private User transferFrom;
    private User transferTo;
}
