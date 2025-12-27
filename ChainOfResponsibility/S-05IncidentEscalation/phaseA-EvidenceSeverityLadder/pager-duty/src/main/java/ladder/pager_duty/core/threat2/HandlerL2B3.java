package ladder.pager_duty.core.threat2;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import ladder.pager_duty.dto.ChainVerdict;
import ladder.pager_duty.dto.Evidence;
import ladder.pager_duty.enums.ThreatLevel;
import ladder.pager_duty.model.Handler;
import ladder.pager_duty.store.Incident;

@Component
@Order(102)
public class HandlerL2B3 implements Handler {

    @Override
    public ChainVerdict atomicExecution(Incident incident) {
        // Pass: when one of the service is "Manager"
        boolean flag = incident.getEvidences().stream()
                .map(Evidence::getServiceName)
                .anyMatch(x -> x.equalsIgnoreCase("Manager"));
        ChainVerdict verdict = ChainVerdict.builder().handlerPassed(flag).build();
        return verdict.toBuilder().log(verdict.isHandlerPassed()
                    ? "L2-B3: Some evidences are fired by Manager service"
                    : "L2-B3: No evidence from Manager service, need not be escalated further").build();
    }

    @Override public ThreatLevel handlerLevel() {
        return ThreatLevel.THREAT_LEVEL_2;
    }
}
