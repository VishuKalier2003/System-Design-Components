package reactivepipe.core;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import reactivepipe.data.Data;
import reactivepipe.data.QueueStatus;
import reactivepipe.data.StateData;
import reactivepipe.database.Activity;
import reactivepipe.model.AbstractQueue;
import reactivepipe.model.Handler;

@Getter
@Setter
@Component
public class KycHandler extends AbstractQueue implements Runnable, Handler {
    private final String handlerID;
    private final Executor executor;
    private Handler nextNode;

    private BiConsumer<String, Data> callback;

    @Autowired private Activity activity;

    public KycHandler(@Qualifier("kycExecutor") Executor executor) {
        this.handlerID = "kyc";
        this.executor = executor;
    }

    @PostConstruct
    public void startWorker() {
        // reference to current handler instance to ensure that the thread created executes the run() of this instance only
        Thread t = new Thread(this);
        t.setName(handlerID + "-worker");
        t.start();      // starting the thread
    }

    @Override public void updateState(StateData stateData) {
        activity.insertOrUpdate(stateData.getTransactionID(), QueueStatus.AMT);
    }

    @Override public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                performOperation(queue.take()).whenCompleteAsync((x, ex) -> {
                    if(ex != null) {
                        System.out.println(ex.getCause());
                        return;
                    }
                    if(isAvailable()) {
                        System.out.println("status updated..."+System.currentTimeMillis());
                        updateState(x.convertToStateData());
                        System.out.println("callback fired to next handler..."+System.currentTimeMillis());
                        callback.accept(handlerID, x);
                    } else {
                        System.out.println("status updated with backpressure...");
                        activity.insertOrUpdate(x.getTransactionID(), QueueStatus.BACKPRESSURE_FAILURE);
                        // the handler passed is "stop", which leads to null ultimately ending the chain safely
                        callback.accept("stop", x);
                    }
                }, this.executor);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Override public CompletableFuture<Data> performOperation(Data inp) {
        return CompletableFuture.completedFuture(inp).thenApplyAsync(x -> {
            try {
                System.out.println("kyc started..."+System.currentTimeMillis());
                Thread.sleep(10000);
                System.out.println("kyc ended..."+System.currentTimeMillis());
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return x;
        }, this.executor);
    }

    @Override public void enqueue(Data data) {queue.add(data);}

    @Override public void setCallback(BiConsumer<String, Data> c) {this.callback = c;}

    @Override public String getHandlerID() {return this.handlerID;}
}
