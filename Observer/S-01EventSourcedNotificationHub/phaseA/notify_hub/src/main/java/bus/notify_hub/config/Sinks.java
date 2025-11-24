package bus.notify_hub.config;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import bus.notify_hub.global.EventEnum;
import bus.notify_hub.model.SinkNode;

@Configuration
public class Sinks {

    private SinkNode createAuditSink() {
        return new SinkNode("AUDIT");
    }

    private SinkNode createBuySink() {
        return new SinkNode("BUY");
    }

    private SinkNode createSellSink() {
        return new SinkNode("SELL");
    }

    @Bean("sinkRegistry")
    public Map<EventEnum, SinkNode> createRegistry() {
        Map<EventEnum, SinkNode> mp = new EnumMap<>(EventEnum.class);
        mp.put(EventEnum.AUDIT, createAuditSink());
        mp.put(EventEnum.BUY, createBuySink());
        mp.put(EventEnum.SELL, createSellSink());
        return mp;
    }
}
