package distributed.saga.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import distributed.saga.business.IdentityService;
import distributed.saga.business.KycService;
import distributed.saga.business.StatusScoreService;
import distributed.saga.model.DataService;

@Configuration
public class ServiceConfig {

    @Bean("services")
    public Map<String, DataService> createServiceMap(IdentityService iS, KycService kyc, StatusScoreService sS) {
        Map<String, DataService> mp = new HashMap<>();
        mp.put("identity", iS);
        mp.put("kyc", kyc);
        mp.put("score", sS);
        return mp;
    }
}
