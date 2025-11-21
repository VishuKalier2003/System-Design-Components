package fabric.sharding.model;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import fabric.sharding.data.Data;

public abstract class AbstractQueue {
    private final int SIZE = 5;
    private final AtomicInteger activeElements = new AtomicInteger();
    protected final LinkedBlockingQueue<Data> queue = new LinkedBlockingQueue<>(SIZE);

    public boolean insert(Data data) {return queue.offer(data);}

    public void increment() {activeElements.incrementAndGet();}
    public void decrement() {activeElements.decrementAndGet();}

    protected int getActiveElements() {return activeElements.get();}

    public abstract void updateMetric(Data data);
}
