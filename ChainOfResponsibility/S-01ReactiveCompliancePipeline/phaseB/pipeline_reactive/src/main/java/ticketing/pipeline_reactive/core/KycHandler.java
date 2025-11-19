package ticketing.pipeline_reactive.core;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import jakarta.annotation.PostConstruct;
import ticketing.pipeline_reactive.data.Data;
import ticketing.pipeline_reactive.database.Database;
import ticketing.pipeline_reactive.model.AbstractQueue;
import ticketing.pipeline_reactive.model.Handler;

public class KycHandler extends AbstractQueue implements Handler, Runnable {
    private final String ID = "kyc";
    private final Logger log = LoggerFactory.getLogger(KycHandler.class);
    private Handler nextNode;

    @Autowired @Qualifier("kycExecutor") private Executor executor;
    @Autowired private Database db;

    @PostConstruct
    public void init() {
        Thread thread = new Thread(this, "kyc-runnable");
        thread.start();
        Thread healer = new Thread(this::healingLoop, "kyc-Healer");
        healer.start();
    }

    private void healingLoop() {
        while(true) {
            try {
                if(paused) {
                    Thread.sleep(waitTime.get());
                    waitTime.set(Math.min(MAX_WAIT, waitTime.get() * 2));
                    if (sustained()) {
                        paused = false;
                        waitTime.set(INITIAL_WAIT);
                        log.info("HEALING SUCCESS → handler {} resumed", ID);
                    }
                }
                Thread.sleep(50);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // AbstractQueue function
    @Override public boolean backpressure() {
        if(!paused) {
            paused = true;
            log.warn("BACKPRESSURE → handler {} paused, waiting for healing...", ID);
        }
        return false;
    }

    // AbstractQueue function
    @Override public boolean sustained() {return burstFactor() < 0.95;}

    Semaphore capacity = new Semaphore(4);      // single permit semaphore (binary semaphore)

    @Override public void run() {
        while (true) {
            try {
                // If paused, do not dequeue
                if (paused) {
                    Thread.sleep(50);   // very light async wait
                    continue;
                }
                // check sustained (queue almost full)
                if (!sustained()) {
                    backpressure();
                    continue;
                }
                capacity.acquire();
                Data data = queue.take();
                performOperation(data).whenComplete((x, ex) -> {
                    capacity.release();
                    if (ex != null)
                        log.error("Handler {} error {}", ID, ex.getMessage());
                    if(next() != null) {
                        log.info("Sending data to the next {} Handler",next().getHandlerID());
                        next().insert(data);
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Handler function
    @Override public CompletableFuture<Data> performOperation(Data inp) {
        return CompletableFuture.completedFuture(inp).thenApplyAsync(x -> {
            try {
                db.insert(x.getTransactionID(), x);
                log.info("Kyc operation initiated...");
                x.getLogs().put("kyc", "operation done");
                x.setAmount(Math.max(0, x.getAmount() - 100));
                Thread.sleep(1500);
                log.info("Kyc operation completed...");
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return x;
        }, executor).exceptionally(fn -> {
            if(fn != null)
                log.info("Error caused in Handler {}, due to {}",ID,fn.getCause());
            return inp;
        });
    }

    @Override public void insert(Data data) {insertIntoQueue(data);}

    @Override public String getHandlerID() {return ID;}
    @Override public void next(Handler handler) {this.nextNode = handler;}
    @Override public Handler next() {return this.nextNode;}
}
