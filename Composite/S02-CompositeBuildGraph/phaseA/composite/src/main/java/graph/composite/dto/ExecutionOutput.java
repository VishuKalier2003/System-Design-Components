package graph.composite.dto;

import java.util.concurrent.ConcurrentLinkedQueue;

import graph.composite.enums.ExecutionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExecutionOutput {
    private String executionID;
    private ExecutionStatus status;
    private ConcurrentLinkedQueue<String> logs;

    public String print() {
        return executionID + "\n" + status + "\n" + logs + "\n";
    }
}
