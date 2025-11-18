package distributed.saga.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request {
    private String user;
    private String aadhar;
    private int age;
}
