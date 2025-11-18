package distributed.saga.model;

import distributed.saga.data.CommandData;
import distributed.saga.data.Data;

public interface Handler {
    public CommandData createCommand(Data data);

    public Handler next();

    public void next(Handler next);

    public String getService();
}
