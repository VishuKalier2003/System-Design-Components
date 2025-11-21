package fabric.sharding.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fabric.sharding.core.Shard;
import fabric.sharding.data.Data;
import fabric.sharding.data.Table;
import fabric.sharding.router.Router;
import jakarta.annotation.PostConstruct;
import lombok.Setter;

@Setter
@Component
public class ShardManager extends Router implements Runnable {
    private final LinkedBlockingQueue<Data> tenantQueue = new LinkedBlockingQueue<>();

    @Autowired @Qualifier("shardRouter") private Map<String, Shard> shardMap;

    private final Logger log = LoggerFactory.getLogger(ShardManager.class);

    public boolean insertIntoTenantQueue(Data data) {return tenantQueue.offer(data);}

    @PostConstruct
    public void init() {
        Thread thread = new Thread(this, "shard-manager");
        thread.start();
    }

    @Override public void run() {
        while(true) {
            try {
                Data data = tenantQueue.take();
                Shard shard = getRespectiveShard(data);
                data.setHash(normalizedHash(data.getTransactionID()));
                if(shard == null)
                    log.info("There does not exist any Shard partition for the hash {}",data.getHash());
                else {
                    boolean added = shard.getHead().pushIntoQueue(data);
                    if(added)
                        log.info("OPERATION started for ID {} at shard {} with hashCode {}",data.getTransactionID(),shard.getShardID(),data.getHash());
                    else {
                        log.warn("QUEUE FULL for ID {} at shard {} with hashCode {}",data.getTransactionID(),shard.getShardID(),data.getHash());
                    }
                }
            } catch(InterruptedException e) {Thread.currentThread().interrupt();}
        }
    }

    public void shiftLower(String shardID, double value) {
        shardMap.get(shardID).setLowerPercentile(value);
    }

    public void shiftHigher(String shardID, double value) {
        shardMap.get(shardID).setHigherPercentile(value);
    }

    public Table showShard(String shardID) {
        return shardMap.get(shardID).showSelf();
    }

    public List<Table> showAllShards() {
        return shardMap.values().stream().map(Shard::showSelf).toList();
    }
}
