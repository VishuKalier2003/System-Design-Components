package distributed.saga.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorConfig {

    @Bean("identity")
    public ExecutorService createIdentityThreads() {return Executors.newFixedThreadPool(3);}

    @Bean("kyc")
    public ExecutorService createKycThreads() {return Executors.newFixedThreadPool(2);}

    @Bean("score")
    public ExecutorService createScoreThreads() {return Executors.newFixedThreadPool(3);}

    @Bean("executors")
    public Map<String, ExecutorService> executors(
        @Qualifier("identity") ExecutorService i,
        @Qualifier("kyc") ExecutorService k,
        @Qualifier("score") ExecutorService s) {
            Map<String, ExecutorService> mp = new HashMap<>();
            mp.put("identity", i);
            mp.put("kyc", k);
            mp.put("score", s);
            return mp;
    }
}
