package fabric.sharding.model;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;

import fabric.sharding.data.Data;
import fabric.sharding.data.MetricData;

public interface Handler {
    public CompletableFuture<Data> evaluate(Data input, Executor executor);

    public String getHandlerID();

    public boolean pushIntoQueue(Data data);

    public void next(Handler node);
    public void callback(BiConsumer<String, MetricData> callback);

    public Handler next();
}
