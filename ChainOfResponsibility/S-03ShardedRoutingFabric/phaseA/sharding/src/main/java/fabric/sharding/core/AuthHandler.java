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
public class AuthHandler extends AbstractQueue implements Handler, Runnable {
    private final int PROCESS;
    private final Executor EXECUTOR;
    private final String handlerID = "auth", shardID;
    private final Logger log = LoggerFactory.getLogger(AuthHandler.class);

    private Handler nextNode;
    private BiConsumer<String, MetricData> function;

    public AuthHandler(int processTime, Executor executor, String shardID) {
        this.EXECUTOR = executor; this.shardID = shardID;
        this.PROCESS = processTime;
    }

    @Override public void updateMetric(Data data) {
        function.accept(shardID, MetricData.builder().load(data.getAmount()).type(MetricType.LOAD).build());
        function.accept(shardID, MetricData.builder().time(PROCESS).type(MetricType.TIME).build());
    }

    @Override public boolean pushIntoQueue(Data data) {return queue.offer(data);}

    @PostConstruct
    public void init() {
        Thread thread = new Thread(this, "auth");
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
                    else
                        log.debug("NULL POINTER from Auth handler to Charges handler");
                });
            }
            catch(InterruptedException e) {Thread.currentThread().interrupt();}
        }
    }

    @Override public CompletableFuture<Data> evaluate(Data data, Executor executor) {
        return CompletableFuture.completedFuture(data).thenApplyAsync(x -> {
            try {
                if(!Character.isUpperCase(x.getCustomer().charAt(0)))
                    return x;
                log.info("Auth Started for transaction ID {} in shard {}",x.getTransactionID(),shardID);
                Thread.sleep(PROCESS);
                log.info("Auth Ended for transaction ID {} in shard {}",x.getTransactionID(),shardID);
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
