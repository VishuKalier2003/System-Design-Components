package graph.composite.core.resource;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import graph.composite.model.Resource;

@Component
public class FuelResource implements Resource {
    private final AtomicInteger fuel = new AtomicInteger(1000);

    public int assignFuel(int value) {
        fuel.addAndGet(-value);
        return value;
    }

    @Override public void requestResource() {
        fuel.addAndGet(1000);
    }

    @Override public boolean checkResource() {
        return fuel.get() > 200;
    }
}
