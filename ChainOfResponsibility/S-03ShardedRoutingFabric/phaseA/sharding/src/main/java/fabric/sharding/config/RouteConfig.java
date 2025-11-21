package fabric.sharding.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fabric.sharding.core.Shard;

@Configuration
public class RouteConfig {

    @Bean("shardRouter")
    public Map<String, Shard> createMetricRouter(
        @Qualifier("shard1") Shard s1, @Qualifier("shard2") Shard s2,
        @Qualifier("shard3") Shard s3, @Qualifier("shard4") Shard s4) {
            LinkedHashMap<String, Shard> mp = new LinkedHashMap<>();
            mp.put("shard1", s1);
            mp.put("shard2", s2);
            mp.put("shard3", s3);
            mp.put("shard4", s4);
        return mp;
    }
}
