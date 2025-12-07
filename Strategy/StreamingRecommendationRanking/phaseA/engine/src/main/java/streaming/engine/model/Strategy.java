package streaming.engine.model;

import java.util.List;
import java.util.function.BiFunction;

import streaming.engine.data.input.Request;
import streaming.engine.data.output.Output;
import streaming.engine.enums.data.RailType;

public interface Strategy {
    public BiFunction<Request, RailType, List<Output>> strategy();
}
