package transactions.compensating.model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import transactions.compensating.data.output.Output;

public class AbstractQueue {
    private static final int MAX = 5;
    
    protected final BlockingQueue<Output> queue = new ArrayBlockingQueue<>(MAX);

    public synchronized void insert(Output output) {queue.add(output);}

    private synchronized boolean full() {return queue.size() >= 5;}

    public boolean backpressure() {return full();}

    protected Output poll() throws InterruptedException  {return queue.take();}
}
