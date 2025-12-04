package graph.task.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import graph.task.data.input.ActionInput;

// Retry will fire the same object back (hence no change in other data of the object)
@Service
public class Retry {

    public ActionInput resetFull(ActionInput inp) {
        return inp.toBuilder().index(new AtomicInteger(0)).retryCount(inp.getRetryCount()+1).retryFlag(true).build();
    }

    public ActionInput resetHalf(ActionInput inp) {
        return inp.toBuilder().index(new AtomicInteger(inp.getActions().size() / 2)).retryCount(inp.getRetryCount()+1).retryFlag(true).build();
    }

    public ActionInput resetQuarter(ActionInput inp) {
        return inp.toBuilder().index(new AtomicInteger(inp.getActions().size() / 4)).retryCount(inp.getRetryCount()+1).retryFlag(true).build();
    }
}
