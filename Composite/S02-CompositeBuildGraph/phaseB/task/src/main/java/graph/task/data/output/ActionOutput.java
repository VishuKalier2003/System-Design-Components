package graph.task.data.output;

import java.util.List;

import graph.task.enums.ActionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder=true)
public class ActionOutput {
    private ActionStatus actionStatus;
    private List<String> logs;
    private int code;
}
