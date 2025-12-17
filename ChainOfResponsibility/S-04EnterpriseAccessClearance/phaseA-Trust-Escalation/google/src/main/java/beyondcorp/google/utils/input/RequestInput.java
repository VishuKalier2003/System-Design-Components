package beyondcorp.google.utils.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
// fixed: add a constructor so that the API can be fed with the objects
public class RequestInput {
    private String tkn;
    private Object input;
}
