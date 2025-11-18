package distributed.saga.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder    // builder pattern implemented
public class CommandData {
    private CommandState commandState;
    private String targetBusiness;      // the service to target to
    private Data data;
}
