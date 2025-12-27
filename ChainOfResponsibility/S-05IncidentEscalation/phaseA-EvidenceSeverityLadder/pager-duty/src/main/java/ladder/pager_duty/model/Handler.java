package ladder.pager_duty.model;

import ladder.pager_duty.dto.ChainVerdict;
import ladder.pager_duty.enums.ThreatLevel;
import ladder.pager_duty.store.Incident;

public interface Handler {
    public ChainVerdict atomicExecution(Incident incident);
    public ThreatLevel handlerLevel();
}
