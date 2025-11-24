package bus.notify_hub.model;

import java.util.concurrent.atomic.AtomicInteger;

import bus.notify_hub.data.Data;
import bus.notify_hub.global.EventEnum;
import lombok.Getter;

@Getter
public class SinkNode {
    private final EventEnum sinkEnum;
    private final AtomicInteger currentVersion, currentAmount;

    public SinkNode(String type) {
        this.sinkEnum = EventEnum.valueOf(type.toUpperCase());
        this.currentVersion = new AtomicInteger(0);
        this.currentAmount = new AtomicInteger(0);
    }

    private void incrementVersion() {currentVersion.incrementAndGet();}

    public final boolean update(Data data) {
        try {
            incrementVersion();
            currentAmount.addAndGet(data.getAmount());
            return true;
        } catch(Exception e) {
            e.getLocalizedMessage();
            return false;
        }
    }
}
