package bus.notify_hub.component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import bus.notify_hub.core.Observer;
import bus.notify_hub.data.Data;
import bus.notify_hub.global.EventEnum;
import bus.notify_hub.model.SinkNode;
import bus.notify_hub.service.EventHub;
import bus.notify_hub.service.Graph;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
public class HubManager extends EventHub implements Runnable {
    @Autowired @Qualifier("sinkRegistry") private Map<EventEnum, SinkNode> registry;
    @Autowired private Graph g;

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public boolean insertEvent(Data data) {
        return pushIntoHub(data);
    }

    public SinkNode get(String type) {return registry.get(EventEnum.valueOf(type.toUpperCase()));}

    @Override protected Data extract() {
        try {
            return eventBus.take();
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Override public void run() {
        Data data = extract();
        SinkNode sink = registry.get(data.getEventEnum());
        boolean done = sink.update(data);
        if(done) {
            CompletableFuture.supplyAsync(() -> data, executor).thenAccept(x -> {
                Set<Observer> obs = g.getActiveObservers(x.getPublisherID().toString());
                int syncVersion = sink.getCurrentVersion().get();
                for(Observer o : obs) {
                    if(Math.abs(o.getVersion().get() - syncVersion) > 1) {
                        // The observer is lagging behind
                        lag(o, sink);
                    }
                    else {
                        // The observer needs to be updated with data only, not with the history...
                        push(o, data);
                    }
                }
            });
        }
    }

    private void lag(Observer observer, SinkNode node) {
        observer.getAmount().set(node.getCurrentAmount().get());
        observer.setLatestEvent(node.getSinkEnum());
        observer.getVersion().set(node.getCurrentVersion().get());
    }

    private void push(Observer observer, Data data) {
        observer.getAmount().addAndGet(data.getAmount());
        observer.setLatestEvent(data.getEventEnum());
        observer.getVersion().incrementAndGet();
    }
}
