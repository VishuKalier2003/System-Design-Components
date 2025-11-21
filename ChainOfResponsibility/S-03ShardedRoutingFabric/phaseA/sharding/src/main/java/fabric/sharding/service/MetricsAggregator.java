package fabric.sharding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import fabric.sharding.core.Shard;
import fabric.sharding.data.MetricData;

@Service
public class MetricsAggregator {

    @Autowired private ApplicationContext context;

    public void pushMetrics(String shardID, MetricData metric) {
        @SuppressWarnings("null")
        Shard shard = context.getBean(shardID, Shard.class);
        switch(metric.getType()) {
            case LOAD -> {
                shard.getMetrics().increaseLoad(metric.getLoad());
            }
            case TIME -> {
                shard.getMetrics().increaseActivity(metric.getTime());
            }
            case HASH -> {
                shard.getRouter().updateByOne(metric.getHash());
            }
            default -> {
                System.out.println("Wrong metric submission, check the Handlers and their configuration!!!");
            }
        }
    }
}
