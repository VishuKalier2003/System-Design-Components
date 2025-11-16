package reactivepipe.data;

import java.util.LinkedHashMap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Data {
    private String transactionID;
    private int amount;
    private final LinkedHashMap<String, String> logs;

    public Data() {
        this.logs = new LinkedHashMap<>();
    }

    public StateData convertToStateData() {
        StateData s = new StateData();
        s.setTransactionID(transactionID);
        return s;
    }
}
