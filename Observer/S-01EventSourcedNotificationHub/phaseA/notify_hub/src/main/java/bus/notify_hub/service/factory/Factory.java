package bus.notify_hub.service.factory;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import bus.notify_hub.core.Observer;
import bus.notify_hub.core.Publisher;
import lombok.Getter;

@Component
@Getter
public class Factory {
    private final Map<String, Publisher> pubRegistry = new LinkedHashMap<>();
    private final Map<String, Observer> obsRegistry = new LinkedHashMap<>();

    @Autowired ApplicationContext ctx;

    public String createPublisher() {
        Publisher p = ctx.getBean(Publisher.class);
        pubRegistry.put(p.getPublisherID(), p);
        return "Publisher created with publisher ID "+p.getPublisherID();
    }

    public String createObserver() {
        Observer o = ctx.getBean(Observer.class);
        obsRegistry.put(o.getObserverID(), o);
        return "Observer created with observer ID "+o.getObserverID();
    }

    public Publisher getPublisher(String key) {return pubRegistry.get(key);}

    public Observer getObserver(String key) {return obsRegistry.get(key);}
}
