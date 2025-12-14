package transactions.compensating.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import transactions.compensating.enums.Bank;

@Getter
@Setter
@Builder
public class User {
    private String username;
    private int amount;
    private Bank bank;

    public synchronized void show() {
        System.out.println("Name : "+getUsername()+" Bank : "+getBank()+" Amount : "+getAmount());
    }
}
