package streaming.engine.data.input;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder=true)
// Stores the handler data that will be used to create handlers via factory
public class HandlerInfo {
    private String handlerName;
    private String handlerRailType;
}
