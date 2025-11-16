package reactivepipe.model;

import java.util.concurrent.CompletableFuture;

import reactivepipe.data.Data;

public interface Piping {
    public void afterStageCompletes(String Id, Data data);

    public CompletableFuture<Data> startPipeline(Data data);
}
