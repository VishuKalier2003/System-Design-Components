package reactivepipe.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StateData {
    private String transactionID;
    private QueueStatus status;
}
