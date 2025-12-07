package streaming.engine.data.input;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HandlerInfo {
    private String handlerName;
    private String handlerRailType;
}
