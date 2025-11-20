package ticketing.pipeline_reactive.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorConfig {
    // A fixed pool of threads assigned to each handler

    @Bean("authExecutor")
    public ExecutorService createAuthExecutor() {return Executors.newFixedThreadPool(4);}

    @Bean("kycExecutor")
    public ExecutorService createKycExecutor() {return Executors.newFixedThreadPool(4);}

    @Bean("payExecutor")
    public ExecutorService createPayExecutor() {return Executors.newFixedThreadPool(4);}

    @Bean("ticketExecutor")
    public ExecutorService createTicketExecutor() {return Executors.newFixedThreadPool(3);}
}
