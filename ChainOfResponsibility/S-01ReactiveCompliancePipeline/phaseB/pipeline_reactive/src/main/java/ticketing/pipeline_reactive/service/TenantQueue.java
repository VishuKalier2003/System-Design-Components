package ticketing.pipeline_reactive.service;

import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Service;

import ticketing.pipeline_reactive.data.Data;

@Service
public class TenantQueue {
    private final LinkedBlockingQueue<Data> tenantQueue = new LinkedBlockingQueue<>();

    public boolean insert(Data data) {return tenantQueue.offer(data);}

    public Data extract() {
        try {
            return tenantQueue.take();
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
