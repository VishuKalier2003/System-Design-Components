package transactions.compensating.data;

import lombok.Getter;
import lombok.Setter;
import transactions.compensating.enums.Bank;

@Getter
@Setter
public class User {
    private String username;
    private int amount;
    private Bank bank;
}
