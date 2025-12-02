package graph.composite.core.resource;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import graph.composite.model.Resource;

@Component
public class QuotaResource implements Resource {
    private final AtomicInteger quota = new AtomicInteger(5);

    public int assignQuota(int value) {
        quota.addAndGet(-value);
        return value;
    }

    @Override public void requestResource() {
        quota.addAndGet(5);
    }

    @Override public boolean checkResource() {
        return quota.get() > 2;
    }
}
