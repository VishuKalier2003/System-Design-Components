package reactivepipe.model;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import reactivepipe.data.Data;

public interface Handler {
    public CompletableFuture<Data> performOperation(Data data);

    public String getHandlerID();

    public void enqueue(Data data);

    public void setCallback(BiConsumer<String, Data> callback);
}
