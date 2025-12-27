package ladder.pager_duty.utils;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class IDGenerator {
    private static final String KEYS = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";

    public String generateID() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 15; i++)
            sb.append(KEYS.charAt(ThreadLocalRandom.current().nextInt(0, KEYS.length())));
        return sb.toString();
    }
}
