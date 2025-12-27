package ladder.pager_duty.core.threat2;

import java.time.Instant;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import ladder.pager_duty.dto.ChainVerdict;
import ladder.pager_duty.enums.ThreatLevel;
import ladder.pager_duty.model.Handler;
import ladder.pager_duty.store.Incident;

@Component
@Order(100)
public class HandlerL2B1 implements Handler {

    @Override
    public ChainVerdict atomicExecution(Incident incident) {
        // Pass: when number of evidences are more than 1, within 30s
        long count = incident.getEvidences().stream().filter(
            x -> x.getCreatedAt().plusSeconds(30l).isAfter(Instant.now())
        ).count();
        ChainVerdict verdict = ChainVerdict.builder().handlerPassed(count > 1).build();
        return verdict.toBuilder().log(verdict.isHandlerPassed()
                ? "L2-B1: There are multiple evidences arriving within 30 seconds"
                : "L2-B2: There are fewer evidences lately (not needed to escalate much)").build();
    }

    @Override public ThreatLevel handlerLevel() {
        return ThreatLevel.THREAT_LEVEL_2;
    }
}
