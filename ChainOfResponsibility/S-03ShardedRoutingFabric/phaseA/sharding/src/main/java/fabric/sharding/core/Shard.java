package fabric.sharding.core;

import fabric.sharding.data.Data;
import fabric.sharding.data.Metrics;
import fabric.sharding.data.RouteCounter;
import fabric.sharding.data.Table;
import fabric.sharding.model.Handler;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Shard {
    private double lowerPercentile, higherPercentile;

    private final RouteCounter router = new RouteCounter();
    private final Metrics metrics = new Metrics();

    private final Handler head;
    private final String shardID;

    public boolean pushIntoEvaluation(Data data) {return this.head.pushIntoQueue(data);}

    public Table showSelf() {
        return Table.builder().lowerBoundary(lowerPercentile).upperBoundary(higherPercentile)
        .activeTime(metrics.getCountTenants().intValue()).load(metrics.getLoadValue())
        .channels(router.getRoutes())
        .build();
    }
}
