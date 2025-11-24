package bus.notify_hub.service;

import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Service;

import bus.notify_hub.data.Data;

@Service
public abstract class EventHub {
    // Ensure abstraction, to hide event bus behavior from publisher and subscribers
    protected final LinkedBlockingQueue<Data> eventBus = new LinkedBlockingQueue<>();

    // Ensuring that the events are added in order
    protected synchronized boolean pushIntoHub(Data data) {
        try {
            return eventBus.offer(data);
        } catch(Exception e) {
            e.getLocalizedMessage();
            return false;
        }
    }

    protected abstract Data extract();
}
