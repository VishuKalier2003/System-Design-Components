package graph.composite.core.components;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import graph.composite.dto.ExecutionOutput;
import graph.composite.enums.ConfigType;
import graph.composite.enums.ExecutionStatus;
import graph.composite.model.ExecuteAction;
import graph.composite.utils.ConfigBlock;

@Component
public class QuotaExecute implements ExecuteAction {
    private final String quotaID = "quota-";
    private final AtomicInteger counter = new AtomicInteger(1);

    @Override
    public Function<ConfigBlock, ExecutionOutput> executionFunction() {
        return config -> {
            ConcurrentLinkedQueue<String> logs = new ConcurrentLinkedQueue<>();
            int q;
            if(config.getConfigType() == ConfigType.QUOTA) {
                q = (int)config.getResource();
            }
            else {
                logs.add("Invalid config type for quota execution");
                return ExecutionOutput.builder().executionID(quotaString()).status(ExecutionStatus.FAIL).logs(logs).build();
            }
            if(q <= 1) {
                logs.add("Quota passed too low : "+q);
                return ExecutionOutput.builder().executionID(quotaString()).status(ExecutionStatus.FAIL).logs(logs).build();
            }
            logs.add("Quota consumed successfully");
            return ExecutionOutput.builder().executionID(quotaString()).status(ExecutionStatus.SUCCESS).logs(logs).build();
        };
    }

    private String quotaString() {
        return quotaID + counter.getAndIncrement();
    }
}
