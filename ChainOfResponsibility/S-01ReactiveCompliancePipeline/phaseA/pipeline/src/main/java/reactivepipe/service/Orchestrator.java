package reactivepipe.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.annotation.PostConstruct;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import reactivepipe.data.Data;
import reactivepipe.model.Handler;
import reactivepipe.model.Piping;

@Setter
@Component
public class Orchestrator implements Piping {

    @Autowired
    @Qualifier("handlerMap")
    private Map<String, Handler> handlerMap;

    @Autowired
    @Qualifier("nextMap")
    private Map<String, String> nextMap;

    private final ConcurrentHashMap<String, CompletableFuture<Data>> futureMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void initCallbacks() {
        handlerMap.forEach((id, handler) -> {
            // attaching the callback function for each handler analogous to (x, y) -> this.afterStageCompletes(id, y)
            handler.setCallback(this::afterStageCompletes);
        });
    }

    @Override
    public CompletableFuture<Data> startPipeline(Data data) {
        CompletableFuture<Data> future = new CompletableFuture<>();
        futureMap.put(data.getTransactionID(), future);
        Handler head = handlerMap.get("auth");   // pipeline entry point
        head.enqueue(data);
        return future;
    }

    @Override
    public void afterStageCompletes(String handlerId, Data data) {
        String nextStage = nextMap.get(handlerId);
        if (nextStage != null) {
            Handler nextHandler = handlerMap.get(nextStage);
            if (nextHandler != null) {
                nextHandler.enqueue(data);
                return;
            }
        }
        // pipeline completed
        CompletableFuture<Data> future = futureMap.get(data.getTransactionID());
        if (future != null)
            future.complete(data);
    }

    public Map<String, CompletableFuture<Data>> getFutureMap() {
        return futureMap;
    }
}
