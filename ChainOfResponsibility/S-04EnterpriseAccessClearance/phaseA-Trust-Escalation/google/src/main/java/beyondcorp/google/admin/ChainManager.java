package beyondcorp.google.admin;

import java.util.List;

import org.springframework.stereotype.Service;

import beyondcorp.google.model.Handler;
import jakarta.annotation.PostConstruct;

@Service
public class ChainManager {
    private final List<Handler> handlers;
    private Handler head;

    // autowiring
    public ChainManager(List<Handler> list) {
        this.handlers = list;
    }

    @PostConstruct
    public void init() {
        head = handlers.get(0);
        int size = handlers.size();
        Handler temp = head;
        for(int i = 1; i < size; i++, temp = temp.next()) {
            temp.next(handlers.get(i));
        }
        temp = head;
        while(temp != null) {
            System.out.println("Handler "+temp.getHandlerUuid());
            temp = temp.next();
        }
    }

    public Handler getHead() {return this.head;}

    public Handler getNext(Handler temp) {return temp.next();}
}
