package graph.task.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import graph.task.enums.func.BaseFunction;
import graph.task.enums.func.CacheFunction;
import graph.task.enums.func.RetryFunction;
import graph.task.model.Marker;

// detail: Configuration class loaded at compile time
@Configuration
public class FunctionProvider {

    @Bean("functions")
    public Map<String, Marker> fMap() {
        Map<String, Marker> map = new HashMap<>();
        map.put("pure", BaseFunction.BASE_METAL_PURE);
        map.put("impure", BaseFunction.BASE_METAL_IMPURE);
        map.put("lru", CacheFunction.LRU_CACHE);
        map.put("full", RetryFunction.FULL);
        map.put("half", RetryFunction.HALF);
        map.put("quarter", RetryFunction.QUARTER);
        return map;
    }
}
