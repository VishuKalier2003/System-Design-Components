package ladder.pager_duty.core.threat3;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import ladder.pager_duty.dto.ChainVerdict;
import ladder.pager_duty.dto.Evidence;
import ladder.pager_duty.enums.ThreatLevel;
import ladder.pager_duty.model.Handler;
import ladder.pager_duty.store.Incident;

@Component
@Order(1000)
public class HandlerL3C1 implements Handler {

    @Override
    public ChainVerdict atomicExecution(Incident incident) {
        // Pass: when one of the service is "Manager"
        boolean flag = incident.getEvidences().stream().mapToDouble(Evidence::getWeight).sum() >= 4;
        ChainVerdict verdict = ChainVerdict.builder().handlerPassed(flag).build();
        return verdict.toBuilder().log(verdict.isHandlerPassed()
                    ? "L3-C1: The confidence summation is satisfactory"
                    : "L3-C1: The confidence summation is too low").build();
    }

    @Override public ThreatLevel handlerLevel() {
        return ThreatLevel.THREAT_LEVEL_3;
    }
}
