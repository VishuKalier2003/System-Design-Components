package fabric.sharding.data;

import fabric.sharding.data.enums.MetricType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MetricData {
    private String shardID;
    private MetricType type;
    private int time;
    private double load, hash;
}
