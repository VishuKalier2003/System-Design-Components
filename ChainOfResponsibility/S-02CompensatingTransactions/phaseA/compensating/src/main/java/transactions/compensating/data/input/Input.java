package transactions.compensating.data.input;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import transactions.compensating.data.User;

@Getter
@Setter
@Builder(toBuilder=true)
// The manager will generate a transactionID using a service
public class Input {
    private int amount;
    private User transferFrom;
    private User transferTo;
}
