package ticketing.pipeline_reactive.model;

import java.util.concurrent.CompletableFuture;

import ticketing.pipeline_reactive.data.Data;

public interface Handler {
    public CompletableFuture<Data> performOperation(Data data);

    public void next(Handler handler);

    public Handler next();

    public void insert(Data data);

    public String getHandlerID();
}
