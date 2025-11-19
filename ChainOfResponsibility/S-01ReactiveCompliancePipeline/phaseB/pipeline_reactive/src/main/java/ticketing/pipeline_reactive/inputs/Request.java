package ticketing.pipeline_reactive.inputs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request {
    private String name;
    private int amount;
    private String accountType;
}
