package beyondcorp.google.store;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder=true)
public class User {
    private String uuid, name, address;
    private long phone;
    private int amount, code;
}
