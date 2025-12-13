package transactions.compensating.utils;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class KeyGenerator {
    private static final String KEYS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public String createKey() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 15; i++)
            sb.append(KEYS.charAt(ThreadLocalRandom.current().nextInt(0, KEYS.length())));
        return sb.toString();
    }
}
