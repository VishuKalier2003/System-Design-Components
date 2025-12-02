package graph.composite.dto;

import graph.composite.enums.OutputStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ManagerOutput {
    private String log;
    private OutputStatus status;
}
