package beyondcorp.google.error;

import beyondcorp.google.model.Actions;

public class AccessFail extends RuntimeException {
    public AccessFail(Actions name, String handler) {
        super("The handler "+name+" fails for authorization by "+handler);
    }
}
