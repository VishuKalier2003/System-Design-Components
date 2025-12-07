package streaming.engine.model;

import java.util.List;

import streaming.engine.data.input.Request;
import streaming.engine.data.output.Output;
import streaming.engine.enums.data.RailType;

public interface Handler {
    public void setCurrentStrategy(Strategy strategy);

    public RailType getCurrentRailType();

    public Strategy getCurrentStrategy();

    public List<Output> executeStrategy(Request request);
}
