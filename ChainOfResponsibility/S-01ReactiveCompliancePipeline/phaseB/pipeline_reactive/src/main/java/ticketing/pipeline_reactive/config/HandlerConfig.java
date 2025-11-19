package ticketing.pipeline_reactive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ticketing.pipeline_reactive.core.AuthHandler;
import ticketing.pipeline_reactive.core.KycHandler;
import ticketing.pipeline_reactive.core.PayHandler;
import ticketing.pipeline_reactive.model.Handler;

@Configuration
public class HandlerConfig {

    @Bean("auth")
    public Handler createAuthHandler() {return new AuthHandler();}

    @Bean("kyc")
    public Handler createKycHandler() {return new KycHandler();}

    @Bean("pay")
    public Handler createPayHandler() {return new PayHandler();}
}
