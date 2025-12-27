package ladder.pager_duty.config;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ladder.pager_duty.enums.ThreatLevel;

@Configuration
public class Config {

    @Bean("threat")
    public Map<ThreatLevel, Integer> threatMapping() {
        Map<ThreatLevel, Integer> mp = new EnumMap<>(ThreatLevel.class);
        mp.put(ThreatLevel.THREAT_LEVEL_1, 1);
        mp.put(ThreatLevel.THREAT_LEVEL_2, 2);
        mp.put(ThreatLevel.THREAT_LEVEL_3, 3);
        return mp;
    }

    @Bean("nextThreatLevel")
    public Map<Integer, AtomicInteger> threatLevelMapping() {
        Map<Integer, AtomicInteger> map = new HashMap<>();
        map.put(1, new AtomicInteger(2));
        map.put(2, new AtomicInteger(3));
        map.put(3, new AtomicInteger(-1));
        return map;
    }
}
