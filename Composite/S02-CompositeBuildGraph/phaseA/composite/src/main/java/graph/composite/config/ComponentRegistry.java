package graph.composite.config;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import graph.composite.core.components.FuelExecute;
import graph.composite.core.components.QuotaExecute;
import graph.composite.core.components.TokenExecute;
import graph.composite.dto.ExecutionOutput;
import graph.composite.enums.ConfigType;
import graph.composite.utils.ConfigBlock;

@Configuration
public class ComponentRegistry {
    @Autowired private FuelExecute fe;
    @Autowired private QuotaExecute qe;
    @Autowired private TokenExecute te;

    @Bean("action_factory")
    public Map<ConfigType, Function<ConfigBlock, ExecutionOutput>> actionFactoryMap() {
        Map<ConfigType, Function<ConfigBlock, ExecutionOutput>> factoryMap = new EnumMap<>(ConfigType.class);
        factoryMap.put(ConfigType.FUEL, fe.executionFunction());
        factoryMap.put(ConfigType.QUOTA, qe.executionFunction());
        factoryMap.put(ConfigType.TOKEN, te.executionFunction());
        return factoryMap;
    }
}
