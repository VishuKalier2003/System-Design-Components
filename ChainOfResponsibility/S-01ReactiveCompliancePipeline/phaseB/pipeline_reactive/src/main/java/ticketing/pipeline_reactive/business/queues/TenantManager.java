package ticketing.pipeline_reactive.business.queues;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import ticketing.pipeline_reactive.business.tickets.TicketManager;
import ticketing.pipeline_reactive.data.Data;
import ticketing.pipeline_reactive.data.enums.AccountType;
import ticketing.pipeline_reactive.exception.NoTicketException;
import ticketing.pipeline_reactive.model.Handler;
import ticketing.pipeline_reactive.service.TenantQueue;

@Service
public class TenantManager implements Runnable {
    @Autowired private TenantQueue tenantQueue;
    @Autowired private TicketManager ticketManager;
    @Autowired @Qualifier("ticketExecutor") private Executor ticketExecutor;
    @Autowired @Qualifier("auth") private Handler auth;
    @Autowired @Qualifier("kyc") private Handler kyc;
    @Autowired @Qualifier("pay") private Handler pay;

    private final Logger log = LoggerFactory.getLogger(TenantManager.class);

    @PostConstruct
    public void init() {
        Thread thread = new Thread(this, "ticket-and-tenant");
        thread.start();
        auth.next(kyc);
        kyc.next(pay);
    }

    public void attachTicket(Data data) {
        CompletableFuture.supplyAsync(() -> data).thenApplyAsync(x -> {
            if(x.getAccountType() == AccountType.BASIC) {
                String basic = ticketManager.getBasicTicket();
                if(basic != null)
                    x.setTicket(basic);
                else
                    throw new NoTicketException(AccountType.BASIC.name());
            } else {
                String premium = ticketManager.getPremiumTicket();
                if(premium != null)
                    x.setTicket(premium);
                else
                    throw new NoTicketException(AccountType.PREMIUM.name());
            }
            tenantQueue.insert(x);
            log.info("The Data with transaction ID {} and {} ticket {} is dispatched to working queue",x.getTransactionID(),x.getAccountType().name(),x.getTicket());
            return x;
        }, ticketExecutor).whenComplete((x, ex) -> {
            if (ex != null) {
                Throwable cause = ex.getCause();
                if(cause instanceof NoTicketException) {
                    log.warn("No ticket available: {}", cause.getMessage());
                } else {
                    log.error("Unexpected error !!");
                }
                return; // DO NOT dereference x
            }
            // success case
            log.debug("Ticket assigned for {}", x.getTransactionID());
        });
    }

    @Override public void run() {
        while(true) {
            Data data = tenantQueue.extract();
            Handler head = auth;
            head.insert(data);
        }
    }
}
