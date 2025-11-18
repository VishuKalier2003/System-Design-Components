package distributed.saga.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import distributed.saga.core.IdentityHandler;
import distributed.saga.core.KycHandler;
import distributed.saga.core.ScoreHandler;
import distributed.saga.model.Handler;
import jakarta.annotation.PostConstruct;

@Component
public class Manager {
    private Handler head;
    private @Autowired @Qualifier("identityHandler") IdentityHandler h1;
    private @Autowired @Qualifier("kycHandler") KycHandler h2;
    private @Autowired @Qualifier("scoreHandler") ScoreHandler h3;

    @PostConstruct
    public void createChain() {
        if(head == null)
            head = h1;
        head.next(h2);
        h2.next(h3);
    }

    public Handler getHead() {return this.head;}
}
