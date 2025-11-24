package bus.notify_hub.core;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import bus.notify_hub.data.Data;
import bus.notify_hub.global.EventEnum;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@Scope("prototype")
public class Observer {
    private AtomicInteger amount, quota, version;
    private static final AtomicInteger COUNTER = new AtomicInteger();
    private EventEnum latestEvent;
    private String observerID;

    public Observer() {
        this.version = new AtomicInteger(0);
        this.amount = new AtomicInteger(0);
        this.quota = new AtomicInteger(5);
    }

    @PostConstruct
    public void init() {
        this.version = new AtomicInteger(0);
        this.amount = new AtomicInteger(0);
        this.quota = new AtomicInteger(5);
        this.observerID = "ob-"+COUNTER.incrementAndGet();
        this.latestEvent = EventEnum.AUDIT;
    }

    public synchronized boolean updateObserver(Data data) {
        amount.addAndGet(data.getAmount());
        int q = quota.decrementAndGet();
        latestEvent = data.getEventEnum();
        version.incrementAndGet();
        if(q == 0) {
            // add technique to effectively unsubscribe the observer
        }
        return true;
    }

    public void resetQuota() {quota.set(5);}
}
