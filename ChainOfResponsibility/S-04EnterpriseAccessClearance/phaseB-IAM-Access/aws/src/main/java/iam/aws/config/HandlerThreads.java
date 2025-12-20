package iam.aws.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerThreads {

    @Bean(name="threads")
    public Map<String, Executor> threads() {
        Map<String, Executor> mp = new HashMap<>();
        mp.put("H1", Executors.newFixedThreadPool(3));
        mp.put("H2", Executors.newFixedThreadPool(2));
        mp.put("E2", Executors.newFixedThreadPool(3));
        mp.put("E1", Executors.newFixedThreadPool(3));
        mp.put("E3", Executors.newFixedThreadPool(3));
        return mp;
    }
}
