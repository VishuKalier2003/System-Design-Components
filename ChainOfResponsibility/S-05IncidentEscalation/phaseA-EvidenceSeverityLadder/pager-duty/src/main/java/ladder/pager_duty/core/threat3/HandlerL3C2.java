package ladder.pager_duty.core.threat3;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import ladder.pager_duty.dto.ChainVerdict;
import ladder.pager_duty.enums.ThreatLevel;
import ladder.pager_duty.model.Handler;
import ladder.pager_duty.store.Incident;

@Component
@Order(1001)
public class HandlerL3C2 implements Handler {

    @Override
    public ChainVerdict atomicExecution(Incident incident) {
        // Pass: when there are at-least 7 evidences
        boolean flag = incident.getEvidences().stream().count() > 6;
        ChainVerdict verdict = ChainVerdict.builder().handlerPassed(flag).build();
        return verdict.toBuilder().log(verdict.isHandlerPassed()
                ? "L3-C2: There are enough evidences, one can escalate to High-risk"
                : "L3-C2: Not enough evidences to escalate further").build();
    }

    @Override public ThreatLevel handlerLevel() {
        return ThreatLevel.THREAT_LEVEL_3;
    }
}
