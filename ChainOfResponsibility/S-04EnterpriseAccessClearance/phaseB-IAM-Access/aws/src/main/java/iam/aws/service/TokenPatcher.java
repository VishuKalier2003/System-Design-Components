package iam.aws.service;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import iam.aws.data.dto.Token;
import iam.aws.enums.ScopeName;

@Component
public class TokenPatcher {
    @Autowired private DegradationGraph dGraph;

    // This generates a Token with a unique Token ID
    public Token createToken(String tokenScope, long degradeSeconds, long activeSeconds) {
        return Token.builder().tokenID(generateTknID())
        .currentScope(ScopeName.valueOf(tokenScope.toUpperCase()))
        .createdAt(Instant.now())
        .degradationClock(degradeSeconds)
        .activeClock(activeSeconds)
        .build();
    }

    public boolean isExpired(Token token) {
        Instant now = Instant.now();
        // checks expiry by current time < created time + active clock
        return now.isBefore(token.getCreatedAt().plusSeconds(token.getActiveClock()));
    }

    // Degrade the token efficiency
    public Token degrade(Token tkn) {
        Instant now = Instant.now(), tknTime = tkn.getCreatedAt().plusSeconds(tkn.getDegradationClock());
        ScopeName current = tkn.getCurrentScope();
        while(now.isBefore(tknTime)) {
            tknTime = tknTime.plusSeconds(tkn.getDegradationClock());
            if(!dGraph.isLeaf(tkn.getCurrentScope()))
                current = dGraph.degrade(current);
        }
        return tkn.toBuilder().currentScope(current).build();
    }

    // checks if degradation is required or not
    public boolean needsDegradation(Token tkn) {
        return Instant.now().isBefore(tkn.getCreatedAt().plusSeconds(tkn.getDegradationClock()));
    }

    private static final String KEYS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private String generateTknID() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 15; i++)
            sb.append(KEYS.charAt(ThreadLocalRandom.current().nextInt(0, KEYS.length())));
        return sb.toString();
    }
}
