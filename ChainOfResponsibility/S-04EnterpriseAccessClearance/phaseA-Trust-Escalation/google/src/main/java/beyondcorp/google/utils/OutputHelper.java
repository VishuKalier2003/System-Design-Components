package beyondcorp.google.utils;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import beyondcorp.google.model.Actions;
import beyondcorp.google.store.Output;
import beyondcorp.google.store.Token;

@Service
public class OutputHelper {

    public List<Token> getAllTokens(Output output) {
        return output.getTokens().stream().toList();
    }

    public Map<Actions, Integer> activeTokens(Output output) {
        Instant now = Instant.now();
        return output.getTokens().stream()
                .collect(Collectors.toMap(
                        Token::getFnName,
                        token -> remainingSeconds(token, now),
                        Math::max // deterministic merge
                ));
    }

    private int remainingSeconds(Token token, Instant now) {
        if (token.getExpiresAt().isBefore(now)) {
            return 0;
        }
        return (int) (token.getExpiresAt().getEpochSecond() - now.getEpochSecond());
    }
}
