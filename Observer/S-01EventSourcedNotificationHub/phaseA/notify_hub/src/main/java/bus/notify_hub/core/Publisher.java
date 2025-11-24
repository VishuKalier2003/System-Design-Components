package bus.notify_hub.core;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import bus.notify_hub.component.HubManager;
import bus.notify_hub.data.Data;
import bus.notify_hub.service.Graph;
import jakarta.annotation.PostConstruct;
import lombok.Getter;

@Getter
@Component
@Scope("prototype")
public class Publisher {
    @Autowired private Graph graph;
    private static final AtomicInteger COUNTER = new AtomicInteger();
    private String publisherID;

    @Autowired private HubManager hm;

    @PostConstruct
    public void init() {
        this.publisherID = "pub-"+COUNTER.incrementAndGet();
        graph.createVector(publisherID);
    }

    public void subscribe(Observer observer) {graph.insertObserver(publisherID, observer);}

    public void unsubscribe(Observer observer) {graph.removeObserver(publisherID, observer);}

    public void publish(Data data) {hm.insertEvent(data);}
}
