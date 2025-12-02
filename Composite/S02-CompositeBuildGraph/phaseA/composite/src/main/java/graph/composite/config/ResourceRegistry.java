package graph.composite.config;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import graph.composite.core.resource.FuelResource;
import graph.composite.core.resource.QuotaResource;
import graph.composite.core.resource.TokenResource;
import graph.composite.enums.ConfigType;
import graph.composite.model.Resource;

@Configuration
public class ResourceRegistry {

    @Bean("resources")
    public Map<ConfigType, Resource> resourceMap() {
        Map<ConfigType, Resource> mp = new EnumMap<>(ConfigType.class);
        mp.put(ConfigType.QUOTA, new QuotaResource());
        mp.put(ConfigType.FUEL, new FuelResource());
        mp.put(ConfigType.TOKEN, new TokenResource());
        return mp;
    }
}
