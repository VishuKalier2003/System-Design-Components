package fabric.sharding.config;

import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fabric.sharding.core.AuthHandler;
import fabric.sharding.core.ChargesHandler;
import fabric.sharding.core.PayHandler;
import fabric.sharding.data.MetricData;
import fabric.sharding.model.Handler;
import fabric.sharding.service.MetricsAggregator;

@Configuration
public class ChainConfig {

    @Bean("metricCallback")
    public BiConsumer<String, MetricData> metricCallback(ObjectProvider<MetricsAggregator> provider) {
        return (shardID, event) -> {
            MetricsAggregator aggregator = provider.getIfAvailable();
            if(aggregator != null)
                aggregator.pushMetrics(shardID, event);
        };
    }

    @Bean("chain1")
    public Handler createChain1(BiConsumer<String, MetricData> callback) {
        Handler h1 = new AuthHandler(2000, Executors.newFixedThreadPool(4), "shard1");
        ChargesHandler h2 = new ChargesHandler(1500, Executors.newFixedThreadPool(4), "shard1");
        PayHandler h3 = new PayHandler(2500, Executors.newFixedThreadPool(4), "shard1");
        h1.callback(callback);
        h2.callback(callback);
        h3.callback(callback);
        h1.next(h2);
        h2.next(h3);
        // INFO: Only h1 is managed by Spring since that is returned as object, so for other handlers need to start @PostConstruct manually
        h2.init();
        h3.init();
        return h1;
    }

    @Bean("chain2")
    public Handler createChain2(BiConsumer<String, MetricData> callback) {
        Handler h1 = new AuthHandler(3000, Executors.newFixedThreadPool(4), "shard2");
        ChargesHandler h2 = new ChargesHandler(1000, Executors.newFixedThreadPool(4), "shard2");
        PayHandler h3 = new PayHandler(5000, Executors.newFixedThreadPool(4), "shard2");
        h1.callback(callback);
        h2.callback(callback);
        h3.callback(callback);
        h1.next(h2);
        h2.next(h3);
        // INFO: Only h1 is managed by Spring since that is returned as object, so for other handlers need to start @PostConstruct manually
        h2.init();
        h3.init();
        return h1;
    }

    @Bean("chain3")
    public Handler createChain3(BiConsumer<String, MetricData> callback) {
        Handler h1 = new AuthHandler(2000, Executors.newFixedThreadPool(4), "shard3");
        ChargesHandler h2 = new ChargesHandler(1000, Executors.newFixedThreadPool(4), "shard3");
        PayHandler h3 = new PayHandler(2000, Executors.newFixedThreadPool(4), "shard3");
        h1.callback(callback);
        h2.callback(callback);
        h3.callback(callback);
        h1.next(h2);
        h2.next(h3);
        // INFO: Only h1 is managed by Spring since that is returned as object, so for other handlers need to start @PostConstruct manually
        h2.init();
        h3.init();
        return h1;
    }

    @Bean("chain4")
    public Handler createChain4(BiConsumer<String, MetricData> callback) {
        Handler h1 = new AuthHandler(1000, Executors.newFixedThreadPool(4), "shard4");
        ChargesHandler h2 = new ChargesHandler(3000, Executors.newFixedThreadPool(4), "shard4");
        PayHandler h3 = new PayHandler(3000, Executors.newFixedThreadPool(4), "shard4");
        h1.callback(callback);
        h2.callback(callback);
        h3.callback(callback);
        h1.next(h2);
        h2.next(h3);
        // INFO: Only h1 is managed by Spring since that is returned as object, so for other handlers need to start @PostConstruct manually
        h2.init();
        h3.init();
        return h1;
    }
}
