package transactions.compensating.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import transactions.compensating.core.handlers.CheckHandler;
import transactions.compensating.core.handlers.CompletionHandler;
import transactions.compensating.core.handlers.QuotaHandler;
import transactions.compensating.core.handlers.ReceiverLockHandler;
import transactions.compensating.core.handlers.ReceiverMoneyHandler;
import transactions.compensating.core.handlers.SenderLockHandler;
import transactions.compensating.core.handlers.SenderMoneyHandler;
import transactions.compensating.model.Handler;

@Service
public class ChainManager {
    private Handler head;

    @Autowired private CheckHandler h1;
    @Autowired private QuotaHandler h2;
    @Autowired private SenderLockHandler h3;
    @Autowired private SenderMoneyHandler h4;
    @Autowired private ReceiverLockHandler h5;
    @Autowired private ReceiverMoneyHandler h6;
    @Autowired private CompletionHandler h7;

    @PostConstruct
    public void init() {
        head = h1;
        h1.next(h3);
        h3.next(h4);
        h4.next(h5);
        h5.next(h6);
        h6.next(h2);
        h2.next(h7);
    }

    public void setHandlers(Handler ha, Handler hb) {
        ha.next(hb);
    }

    public Handler getHead() {return this.head;}
}
