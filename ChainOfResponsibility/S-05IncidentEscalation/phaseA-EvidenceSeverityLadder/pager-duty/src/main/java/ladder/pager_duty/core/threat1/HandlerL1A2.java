package ladder.pager_duty.core.threat1;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import ladder.pager_duty.dto.ChainVerdict;
import ladder.pager_duty.dto.Evidence;
import ladder.pager_duty.enums.ThreatLevel;
import ladder.pager_duty.model.Handler;
import ladder.pager_duty.store.Incident;

@Component
@Order(2)       // Ordering
public class HandlerL1A2 implements Handler {

    @Override
    public ChainVerdict atomicExecution(Incident incident) {
        long count = incident.getEvidences().stream().map(Evidence::getWeight).filter(x -> x > 0).distinct().count();
        ChainVerdict verdict = ChainVerdict.builder().handlerPassed(count > 1).build();
        return verdict.toBuilder().log(verdict.isHandlerPassed()
                ? "L1-A2: More than one evidence has positive confidence"
                : "L1-A2: Not enough positive confidence weights from evidences").build();
    }

    @Override public ThreatLevel handlerLevel() {
        return ThreatLevel.THREAT_LEVEL_1;
    }
}
