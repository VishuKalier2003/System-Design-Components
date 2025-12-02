package graph.composite.utils;

import graph.composite.enums.ConfigType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ConfigBlock {
    private ConfigType configType;
    private Integer value;
    private Object resource;
}
