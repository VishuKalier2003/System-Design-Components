package beyondcorp.google.admin;

import org.springframework.stereotype.Service;

import beyondcorp.google.store.Output;
import beyondcorp.google.store.enums.TrustLevel;
import beyondcorp.google.store.enums.TrustPerformance;

@Service
public class TrustEngine {

    public TrustPerformance evaluateTrustPerformance(Output.ChainData data) {
        if(data.getPassed().intValue() < 3)
            return TrustPerformance.RISKY;
        double ratio = (data.getPassed().intValue() + 0.0d) / data.getFailed().intValue();
        if(ratio < 0.75)
            return TrustPerformance.ACCEPTABLE;
        return TrustPerformance.HEALTHY;
    }

    public TrustLevel getCurrentTrustLevel(Output.ChainData data) {
        if(data.getPassed().intValue() >= 5 && data.getPerformance() == TrustPerformance.HEALTHY)
            return TrustLevel.ADMIN;
        else if(data.getPassed().intValue() >= 3 && data.getPerformance() != TrustPerformance.RISKY)
            return TrustLevel.MANAGER;
        return TrustLevel.CUSTOMER;
    }
}
