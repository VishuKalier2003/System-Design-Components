package fabric.sharding.core;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fabric.sharding.data.Data;
import fabric.sharding.data.MetricData;
import fabric.sharding.data.enums.MetricType;
import fabric.sharding.model.AbstractQueue;
import fabric.sharding.model.Handler;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayHandler extends AbstractQueue implements Handler, Runnable {
    private final int PROCESS;
    private final Executor EXECUTOR;
    private final String handlerID = "pay", shardID;
    private final Logger log = LoggerFactory.getLogger(PayHandler.class);

    private Handler nextNode;
    private BiConsumer<String, MetricData> function;

    public PayHandler(int processTime, Executor executor, String shardID) {
        this.EXECUTOR = executor; this.shardID = shardID; this.PROCESS = processTime;
    }

    @Override public void updateMetric(Data data) {
        function.accept(shardID, MetricData.builder().load(data.getAmount()).type(MetricType.LOAD).build());
        function.accept(shardID, MetricData.builder().time(PROCESS).type(MetricType.TIME).build());
        // Hash to be sent as metric only when the chain successfully completes
        function.accept(shardID, MetricData.builder().hash(data.getHash()).type(MetricType.HASH).build());
    }

    @Override public boolean pushIntoQueue(Data data) {return queue.offer(data);}

    @PostConstruct
    public void init() {
        Thread thread = new Thread(this, "pay");
        thread.start();
    }

    @Override public void run() {
        while(true) {
            try {
                Data data = queue.take();
                evaluate(data, EXECUTOR).whenComplete((x, ex) -> {
                    updateMetric(data);
                    if(next() != null)
                        next().pushIntoQueue(data);
                    log.info("MARKING END OF COR TRANSACTION form transaction ID {} in shard ID {}",data.getTransactionID(),shardID);
                });
            }
            catch(InterruptedException e) {Thread.currentThread().interrupt();}
        }
    }

    @Override public CompletableFuture<Data> evaluate(Data data, Executor executor) {
        return CompletableFuture.completedFuture(data).thenApplyAsync(x -> {
            try {
                x.setAmount(x.getAmount() - x.getPay());
                log.info("Pay Started for transaction ID {} in shard {}",x.getTransactionID(),shardID);
                Thread.sleep(PROCESS);
                log.info("Pay Ended for transaction ID {} in shard {}",x.getTransactionID(),shardID);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return x;
        }, EXECUTOR);
    }

    @Override public String getHandlerID() {return handlerID;}
    @Override public void callback(BiConsumer<String, MetricData> callback) {this.function = callback;}

    @Override public void next(Handler next) {this.nextNode = next;}
    @Override public Handler next() {return this.nextNode;}
}
