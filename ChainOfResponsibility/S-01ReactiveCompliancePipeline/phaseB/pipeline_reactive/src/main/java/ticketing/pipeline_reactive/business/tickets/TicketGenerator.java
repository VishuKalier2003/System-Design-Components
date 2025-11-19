package ticketing.pipeline_reactive.business.tickets;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

@Component
public class TicketGenerator {
    private final AtomicInteger premium = new AtomicInteger(1), basic = new AtomicInteger(1);

    public String generateBasicTicket() {
        return "BASIC"+basic.incrementAndGet();
    }

    public String generatePremiumTicket() {
        return "PREMIUM"+premium.incrementAndGet();
    }
}
