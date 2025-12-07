package streaming.engine.utils;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

@Service
public class UUIDgenerator {
    private final AtomicInteger index = new AtomicInteger();

    public String uuid() {
        return "anime-"+index.incrementAndGet();
    }
}
