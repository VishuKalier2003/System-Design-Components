package reactivepipe.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactivepipe.core.AmountHandler;
import reactivepipe.core.AuthHandler;
import reactivepipe.core.KycHandler;
import reactivepipe.core.PayHandler;
import reactivepipe.model.Handler;

@Configuration
public class HandlerConfig {

    @Bean("handlerMap")
    public Map<String, Handler> createHandlerMap(AuthHandler auth, KycHandler kyc, AmountHandler amt, PayHandler pay) {
        Map<String, Handler> mp = new HashMap<>();
        mp.put("auth", auth);
        mp.put("kyc", kyc);
        mp.put("amt", amt);
        mp.put("pay", pay);
        return mp;
    }

    @Bean("nextMap")
    public Map<String, String> createNextMap() {
        Map<String, String> map = new HashMap<>();
        map.put("auth", "kyc");
        map.put("kyc", "amt");
        map.put("amt", "pay");
        map.put("pay", null);
        map.put("stop", null);
        return map;
    }
}
