package ticketing.pipeline_reactive.config;

import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    @Bean(name="basicQueue")
    public LinkedBlockingQueue<String> createLinkedBasicQueue() {
        return new LinkedBlockingQueue<>(100);
    }

    @Bean(name="premiumQueue")
    public LinkedBlockingQueue<String> createLinkedPremiumQueue() {
        return new LinkedBlockingQueue<>(1000);
    }
}
