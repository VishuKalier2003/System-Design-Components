package ticketing.pipeline_reactive.utils;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

@Component
public class Serializer {
    private final AtomicInteger index = new AtomicInteger(0);

    public String generateTransactionID() {
        return "task"+index.incrementAndGet();
    }
}
