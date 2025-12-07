package streaming.engine.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import streaming.engine.core.Experiment.RandomStrategy;
import streaming.engine.core.comfort.DayStrategy;
import streaming.engine.core.comfort.GenreStrategy;
import streaming.engine.core.discovery.ActivityStrategy;
import streaming.engine.model.Strategy;

@Configuration
public class StrategyConfig {
    @Autowired private DayStrategy ds;
    @Autowired private GenreStrategy gs;
    @Autowired private ActivityStrategy as;
    @Autowired private RandomStrategy rs;

    @Bean("RegisteredStrategy")
    public Map<String, Strategy> strategies() {
        Map<String, Strategy> mp = new LinkedHashMap<>();
        mp.put("DayStrategy-max", ds.MAX_DAY);
        mp.put("DayStrategy-all", ds.ALL_DAYS);
        mp.put("GenreStrategy-max", gs.MAX_GENRE);
        mp.put("GenreStrategy-two", gs.GENRE_FOR_TWIN);
        mp.put("GenreStrategy-last", gs.LAST_WATCHED);
        mp.put("ActivityStrategy-high", as.ACTIVITY_HIGH);
        mp.put("ActivityStrategy-low", as.ACTIVITY_LOW);
        mp.put("RandomStrategy-random", rs.RANDOM);
        mp.put("RandomStrategy-neighbor", rs.RANDOM_NEIGHBOR);
        return mp;
    }
}
