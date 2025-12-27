package ladder.pager_duty.core.threat1;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import ladder.pager_duty.dto.ChainVerdict;
import ladder.pager_duty.dto.Evidence;
import ladder.pager_duty.enums.ThreatLevel;
import ladder.pager_duty.model.Handler;
import ladder.pager_duty.store.Incident;

@Component
@Order(1)   // Order used to set ordering
public class HandlerL1A1 implements Handler {

    @Override
    public ChainVerdict atomicExecution(Incident incident) {
        long count = incident.getEvidences().stream().map(Evidence::getServiceName).distinct().count();
        ChainVerdict verdict = ChainVerdict.builder().handlerPassed(count > 1).build();
        return verdict.toBuilder().log(
            verdict.isHandlerPassed() ? "L1-A1: More than one service raised the Evidence" : "L1-A1:Only one service raised the Evidence")
            .build();
    }

    @Override public ThreatLevel handlerLevel() {
        return ThreatLevel.THREAT_LEVEL_1;
    }
}
