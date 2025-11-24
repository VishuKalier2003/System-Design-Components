package bus.notify_hub.data;

import bus.notify_hub.global.EventEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Data {
    private final int amount;
    private final EventEnum eventEnum;
    private final StringBuilder publisherID;

    public Data(int amount, String event) {
        this.amount = amount;
        this.eventEnum = EventEnum.valueOf(event.toUpperCase());
        this.publisherID = new StringBuilder();
    }

    public void attach(String s) {this.publisherID.append(s);}
}
