package graph.composite.core.resource;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import graph.composite.model.Resource;
import jakarta.annotation.PostConstruct;

@Component
public class TokenResource implements Resource {
    private final LinkedBlockingQueue<String> tokenPool = new LinkedBlockingQueue<>(5);
    private final String token = "token";
    private final AtomicInteger tokenCount = new AtomicInteger(1);

    @PostConstruct
    public void initTokenPool() {
        for(int i = 0; i < 5; i++) {
            tokenPool.add(assignToken());
        }
    }

    private String assignToken() {
        StringBuilder assignedToken = new StringBuilder();
        assignedToken.append(ThreadLocalRandom.current().nextInt(10000, 99999)).append("-");
        assignedToken.append(token).append("-").append(tokenCount.getAndIncrement());
        return assignedToken.toString();
    }

    public String acquireToken() {
        return tokenPool.poll();
    }

    @Override public void requestResource() {
        tokenPool.add(assignToken());
        tokenPool.add(assignToken());
    }

    @Override public boolean checkResource() {
        return !tokenPool.isEmpty();
    }
}
