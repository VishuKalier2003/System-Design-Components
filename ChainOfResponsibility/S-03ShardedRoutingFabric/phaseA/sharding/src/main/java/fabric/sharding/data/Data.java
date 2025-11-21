package fabric.sharding.data;

import java.util.LinkedHashMap;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Data {
    // Compulsorily build transactionID, customer, credit rates and amount
    private final String transactionID, customer;
    private boolean authenticated;
    private double hash;
    private final int creditRates;
    private double amount, pay;
    private final LinkedHashMap<String, String> logs;
}
