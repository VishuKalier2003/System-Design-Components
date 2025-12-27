package ladder.pager_duty.core.threat2;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import ladder.pager_duty.dto.ChainVerdict;
import ladder.pager_duty.dto.Evidence;
import ladder.pager_duty.enums.ThreatLevel;
import ladder.pager_duty.model.Handler;
import ladder.pager_duty.store.Incident;

@Component
@Order(101)
public class HandlerL2B2 implements Handler {

    @Override
    public ChainVerdict atomicExecution(Incident incident) {
        // Pass: when the weight of evidence of at-least 2 is more than 0.5
        long count = incident.getEvidences().stream().map(Evidence::getWeight).filter(x -> x > 0.5).count();
        ChainVerdict verdict = ChainVerdict.builder().handlerPassed(count > 1).build();
        return verdict.toBuilder().log(verdict.isHandlerPassed()
                ? "L2-B2: Some evidences are exceedingly positive confidences"
                : "L2-B2: Not much heavy positive confident evidences").build();
    }

    @Override public ThreatLevel handlerLevel() {
        return ThreatLevel.THREAT_LEVEL_2;
    }
}
