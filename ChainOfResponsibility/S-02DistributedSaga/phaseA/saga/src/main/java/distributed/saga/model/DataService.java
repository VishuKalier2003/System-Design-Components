package distributed.saga.model;

import distributed.saga.data.Data;
import distributed.saga.data.State;

public interface DataService {
    public State receive(Data data) throws InterruptedException;

    public State execute(Data data) throws InterruptedException;

    public State compensate(Data data) throws InterruptedException;

    public Data extract(String Id) throws InterruptedException;
}
