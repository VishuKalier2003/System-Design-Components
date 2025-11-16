package reactivepipe.utils;

import org.springframework.stereotype.Component;

@Component
public class Serializer {
    private int index = 0;
    private static final String WORD = "transaction";

    public String generateTransactionID() {
        index++;
        return WORD + index;
    }
}
