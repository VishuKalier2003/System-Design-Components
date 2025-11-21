package fabric.sharding.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {
    private String transactionID, customer;
    private int charges;
    private double amount, pay;
}
