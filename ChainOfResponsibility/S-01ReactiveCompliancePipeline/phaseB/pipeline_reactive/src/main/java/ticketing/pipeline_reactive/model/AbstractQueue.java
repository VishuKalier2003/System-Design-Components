package ticketing.pipeline_reactive.model;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.Setter;
import ticketing.pipeline_reactive.data.Data;

@Getter
@Setter
public abstract class AbstractQueue {
    private int QUEUE_SIZE = 40;
    protected final int MAX_WAIT = 20000, INITIAL_WAIT = 3000;
    protected final AtomicInteger waitTime = new AtomicInteger(INITIAL_WAIT);

    protected volatile boolean paused = false;

    protected LinkedBlockingQueue<Data> queue = new LinkedBlockingQueue<>(QUEUE_SIZE);

    public double burstFactor() {return (queue.size() + 0.0d) / QUEUE_SIZE;}

    public void insertIntoQueue(Data data) {
        try {
            // Log BEFORE blocking
            if (queue.size() == QUEUE_SIZE) {
                System.out.println("[QUEUE FULL] Handler queue is full. tx=" + data.getTransactionID());
            } else {
                System.out.println("[QUEUE INSERT TRY] Attempting insert. tx=" + data.getTransactionID() +" queueSize=" + queue.size());
            }
            // Blocking insert
            this.queue.put(data);
            // Log AFTER successful insert
            System.out.println("[QUEUE INSERTED] Inserted tx=" + data.getTransactionID() +" newQueueSize=" + queue.size());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[QUEUE ERROR] Interrupted when inserting tx=" + data.getTransactionID());
        }
    }

    protected abstract boolean backpressure();

    protected abstract boolean sustained();
}
