package beyondcorp.google.utils;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import beyondcorp.google.model.Actions;
import beyondcorp.google.store.Token;

@Component
public class Tokenizer {

    private static final String KEYS = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";

    public Token createToken(Actions fName, int seconds) {
        Instant issuedAt = Instant.now();
        return Token.builder()
                .tokenID(generateTokenString())
                .fnName(fName)
                .createdAt(issuedAt)
                .expiresAt(issuedAt.plusSeconds(seconds))
                .build();
    }

    public String createToken() {
        return generateTokenString();
    }

    private String generateTokenString() {
        final StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 15; i++)
            sb.append(KEYS.charAt(ThreadLocalRandom.current().nextInt(0, KEYS.length())));
        return sb.toString();
    }
}
