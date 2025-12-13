package transactions.compensating.config;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import transactions.compensating.core.compensators.ReceiverLockCompensator;
import transactions.compensating.core.compensators.ReceiverMoneyCompensator;
import transactions.compensating.core.compensators.SenderLockCompensator;
import transactions.compensating.core.compensators.SenderMoneyCompensator;
import transactions.compensating.enums.Handlers;
import transactions.compensating.model.Compensator;

@Configuration
public class CompensatorMapper {

    @Autowired private ReceiverLockCompensator rlc;
    @Autowired private SenderLockCompensator slc;
    @Autowired private SenderMoneyCompensator smc;
    @Autowired private ReceiverMoneyCompensator rmc;

    @Bean("compensators")
    public Map<Handlers, Compensator> compensators() {
        Map<Handlers, Compensator> map = new EnumMap<>(Handlers.class);
        map.put(Handlers.RECEIVER_LOCK_COMPENSATOR, rlc);
        map.put(Handlers.SENDER_LOCK_COMPENSATOR, slc);
        map.put(Handlers.SENDER_MONEY_COMPENSATOR, smc);
        map.put(Handlers.RECEIVER_MONEY_COMPENSATOR, rmc);
        return map;
    }
}
