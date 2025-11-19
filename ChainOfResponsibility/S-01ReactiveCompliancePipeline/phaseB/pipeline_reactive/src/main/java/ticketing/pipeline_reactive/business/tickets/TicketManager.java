package ticketing.pipeline_reactive.business.tickets;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class TicketManager {
    @Autowired @Qualifier("basicQueue") private LinkedBlockingQueue<String> basicQueue;
    @Autowired @Qualifier("premiumQueue") private LinkedBlockingQueue<String> premiumQueue;

    @Autowired private TicketGenerator ticketGenerator;

    private final Logger log = LoggerFactory.getLogger(TicketManager.class);
    private final AtomicBoolean basic = new AtomicBoolean(false), premium = new AtomicBoolean(false);

    @PostConstruct
    public void warmUp() {
        // INFO: Give some initial quotas to the queues, so that the early consumers do not go into starvation
        for(int i = 0; i <= 15; i++)
            insertIntoBasic();
        for(int i = 0; i <= 50; i++)
            insertIntoPremium();
    }

    public boolean insertIntoBasic() {return basicQueue.offer(ticketGenerator.generateBasicTicket());}
    public boolean insertIntoPremium() {return premiumQueue.offer(ticketGenerator.generatePremiumTicket());}

    private boolean hasBasicTicket() {return !basicQueue.isEmpty();}
    private boolean hasPremiumTicket() {return !premiumQueue.isEmpty();}

    @Scheduled(fixedRate=1000)
    public void pushBasicIntoTenant() {
        if(!insertIntoBasic()) {
            if(!basic.get()) {
                log.warn("BASIC Ticket MAX CAP reached");
                basic.set(true);
            }
        } else {basic.set(false);}
    }

    @Scheduled(fixedRate=500)
    public void pushPremiumIntoTenant() {
        if(!insertIntoPremium()) {
            if(!premium.get()) {
                log.warn("PREMIUM Ticket MAX CAP reached");
                premium.set(true);
            }
        }
        else {premium.set(false);}
    }

    public String getBasicTicket() {return hasBasicTicket() ? basicQueue.poll() : null;}
    public String getPremiumTicket() {return hasPremiumTicket() ? premiumQueue.poll() : null;}
}
