package iam.aws.data.dto;

import java.time.Instant;

import iam.aws.enums.ScopeName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder(toBuilder=true)
public class Token {
    // compulsory tokenID as the uuid
    private final String tokenID;
    // fixed: never use a JPA Table directly, can cause security risk (database uses dto not other way round)
    private ScopeName currentScope;
    // When degradation required set it to true
    private Boolean degradeNow;
    private final Instant createdAt;
    private final long degradationClock, activeClock;
}
