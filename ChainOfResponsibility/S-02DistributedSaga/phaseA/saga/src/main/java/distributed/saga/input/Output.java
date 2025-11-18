package distributed.saga.input;

import distributed.saga.data.CommandState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Output {
    private String transactionID;
    private String currentService;
    private CommandState currentCommand;
}
