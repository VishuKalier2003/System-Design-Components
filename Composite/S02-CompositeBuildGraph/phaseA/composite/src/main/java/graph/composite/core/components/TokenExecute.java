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
public class TokenExecute implements ExecuteAction {
    private final String tokenID = "token-";
    private final AtomicInteger counter = new AtomicInteger(1);

    @Override
    public Function<ConfigBlock, ExecutionOutput> executionFunction() {
        return config -> {
            ConcurrentLinkedQueue<String> logs = new ConcurrentLinkedQueue<>();
            String tkn;
            if(config.getConfigType() == ConfigType.TOKEN) {
                tkn = (String)config.getResource();
            }
            else {
                logs.add("Invalid config type for token execution");
                return ExecutionOutput.builder().executionID(fuelString()).status(ExecutionStatus.FAIL).logs(logs).build();
            }
            if(tkn.length() < 5) {
                logs.add("Token vulnerable (length less than 5) : "+tkn);
                return ExecutionOutput.builder().executionID(fuelString()).status(ExecutionStatus.FAIL).logs(logs).build();
            } else if(!prime(Integer.parseInt(tkn.substring(0, 5)))) {
                logs.add("Token is not prime, cannot proceed : "+tkn);
                return ExecutionOutput.builder().executionID(fuelString()).status(ExecutionStatus.FAIL).logs(logs).build();
            }
            logs.add("Token consumed successfully");
            return ExecutionOutput.builder().executionID(fuelString()).status(ExecutionStatus.SUCCESS).logs(logs).build();
        };
    }

    private String fuelString() {
        return tokenID + counter.getAndIncrement();
    }

    private boolean prime(int fuel) {
        for(int i = 2; i <= Math.sqrt(fuel); i++) {
            if(fuel % i == 0) {
                return true;
            }
        }
        return false;
    }
}
