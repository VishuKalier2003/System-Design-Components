package beyondcorp.google.store;

import java.time.Instant;

import beyondcorp.google.model.Actions;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Token {
    private final String tokenID;
    private final Actions fnName;
    private final Instant createdAt, expiresAt;

    public boolean expired() {
        // fixed: first value lesser than second value
        return Instant.now().isAfter(expiresAt);
    }
}
