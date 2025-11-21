package fabric.sharding.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fabric.sharding.core.Shard;
import fabric.sharding.model.Handler;

@Configuration
public class ShardConfig {

    @Bean("shard1")
    public Shard createShardI(@Qualifier("chain1") Handler head) {
        return Shard.builder().lowerPercentile(0.0d).higherPercentile(0.25d)
        .head(head).shardID("shard1").build();
    }

    @Bean("shard2")
    public Shard createShardII(@Qualifier("chain2") Handler head) {
        return Shard.builder().lowerPercentile(0.25d).higherPercentile(0.50d)
        .head(head).shardID("shard2").build();
    }

    @Bean("shard3")
    public Shard createShardIII(@Qualifier("chain3") Handler head) {
        return Shard.builder().lowerPercentile(0.50d).higherPercentile(0.75d)
        .head(head).shardID("shard3").build();
    }

    @Bean("shard4")
    public Shard createShardIV(@Qualifier("chain4") Handler head) {
        return Shard.builder().lowerPercentile(0.75d).higherPercentile(1.0d)
        .head(head).shardID("shard4").build();
    }
}
