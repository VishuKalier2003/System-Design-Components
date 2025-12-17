package beyondcorp.google.utils.input;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request {
    // detail: using this we will create our Output that will be passed to the chain
    private String uuid;
    private Map<String, String> map;
}
