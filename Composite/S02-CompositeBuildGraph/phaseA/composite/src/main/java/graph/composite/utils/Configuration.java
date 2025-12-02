package graph.composite.utils;

import java.util.concurrent.ConcurrentHashMap;

import graph.composite.enums.ConfigType;
import lombok.Getter;

@Getter
public class Configuration {
    private final ConcurrentHashMap<ConfigType, Integer> configs = new ConcurrentHashMap<>();

    public void addConfiguration(ConfigType type, Integer value) {
        configs.putIfAbsent(type, value);
    }
}
