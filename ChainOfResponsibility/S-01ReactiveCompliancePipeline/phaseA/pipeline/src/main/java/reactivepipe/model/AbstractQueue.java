package reactivepipe.model;

import java.util.concurrent.LinkedBlockingDeque;

import reactivepipe.data.Data;
import reactivepipe.data.StateData;

public abstract class AbstractQueue {
    protected final LinkedBlockingDeque<Data> queue = new LinkedBlockingDeque<>(5);

    public void enqueue(Data data) {queue.add(data);}

    public abstract void updateState(StateData stateData);
}
