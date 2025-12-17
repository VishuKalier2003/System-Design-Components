package beyondcorp.google.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import beyondcorp.google.model.Actions;
import beyondcorp.google.store.Token;
import lombok.Getter;

@Getter
@Service
public class TokenUtility {
    private final Map<Actions, Token> tknMap = new HashMap<>();

    public Map<Actions, Token> view(Set<Token> tokens) {
        return tokens.stream().collect(
            Collectors.toMap(
                Token::getFnName,
                tkn -> tkn,
                (a,b) -> a
            )
        );
    }

    public Optional<Token> get(Actions action) {
        return Optional.ofNullable(tknMap.get(action));
    }
}
