package reactivepipe.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorConfig {

    @Bean("authExecutor")
    public ExecutorService assignThreadsToAuth() {return Executors.newFixedThreadPool(3);}

    @Bean("kycExecutor")
    public ExecutorService assignThreadsToKyc() {return Executors.newFixedThreadPool(3);}

    @Bean("paymentExecutor")
    public ExecutorService assignThreadsToPayment() {return Executors.newFixedThreadPool(2);}

    @Bean("amountExecutor")
    public ExecutorService assignThreadsToAmount() {return Executors.newFixedThreadPool(2);}
}
