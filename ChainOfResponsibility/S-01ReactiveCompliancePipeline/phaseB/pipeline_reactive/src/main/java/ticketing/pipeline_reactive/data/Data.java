package ticketing.pipeline_reactive.data;

import java.util.LinkedHashMap;

import lombok.Getter;
import lombok.Setter;
import ticketing.pipeline_reactive.data.enums.AccountType;

@Getter
@Setter
public class Data {
    private String transactionID, kycID;
    private final String customer;
    private int amount;
    private AccountType accountType;
    private String ticket;
    private final LinkedHashMap<String, String> logs;

    public Data(String customer, int amount, String accType) {
        this.customer = customer;
        this.amount = amount;
        this.accountType = AccountType.valueOf(accType);
        this.logs = new LinkedHashMap<>();
        // specifically set null, to ensure it receives ticket when polled from tenantQueue
        this.ticket = null;
    }
}
