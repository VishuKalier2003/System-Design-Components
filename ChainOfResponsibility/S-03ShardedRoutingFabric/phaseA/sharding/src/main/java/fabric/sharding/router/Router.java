package fabric.sharding.router;

import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fabric.sharding.core.Shard;
import fabric.sharding.data.Data;
import fabric.sharding.utils.ShaHasher;
import lombok.Setter;

@Setter
@Component
public class Router {
    @Autowired private ShaHasher hasher;
    @Autowired @Qualifier("shardRouter") private Map<String, Shard> shardMap;

    private final Map<Double, Shard> deferencedMap = new HashMap<>();

    protected double normalizedHash(String transID) {
        return (hasher.hashToLong(transID) / (Long.MAX_VALUE + 0.0d));
    }

    // INFO: The deferenced routing is static, if ranges are updated, the deferenced routes still work on previous ranges
    public Shard getRespectiveShard(Data data) {
        double normalized = normalizedHash(data.getTransactionID());
        if(deferencedMap.containsKey(normalized))
            return deferencedMap.get(normalized);
        for(Shard shard : shardMap.values()) {
            if(shard.getLowerPercentile() < normalized && shard.getHigherPercentile() >= normalized)
                return shard;
        }
        return null;
    }

    public String getOriginalShardID(String task) {
        double normalized = normalizedHash(task);
        for(Shard shard : shardMap.values()) {
            if(shard.getLowerPercentile() < normalized && shard.getHigherPercentile() >= normalized)
                return shard.getShardID();
        }
        return null;
    }

    public String deferenceRoute(String data, String shard) {
        deferencedMap.put(normalizedHash(data), shardMap.get(shard));
        return deferencedMap.get(normalizedHash(data)).getShardID();
    }
}
