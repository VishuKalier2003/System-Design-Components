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
public class FuelExecute implements ExecuteAction {
    private final String fuelID = "fuel-";
    private final AtomicInteger counter = new AtomicInteger(1);

    @Override
    public Function<ConfigBlock, ExecutionOutput> executionFunction() {
        return config -> {
            ConcurrentLinkedQueue<String> logs = new ConcurrentLinkedQueue<>();
            int f;
            if(config.getConfigType() == ConfigType.FUEL) {
                f = (int)config.getResource();
            }
            else {
                logs.add("Invalid config type for fuel execution");
                return ExecutionOutput.builder().executionID(fuelString()).status(ExecutionStatus.FAIL).logs(logs).build();
            }
            if(f <= 0) {
                logs.add("Fuel amount passed too low : "+f);
                return ExecutionOutput.builder().executionID(fuelString()).status(ExecutionStatus.FAIL).logs(logs).build();
            } else if(prime(f)) {
                logs.add("Fuel amount is prime, cannot proceed : "+f);
                return ExecutionOutput.builder().executionID(fuelString()).status(ExecutionStatus.FAIL).logs(logs).build();
            }
            logs.add("Fuel consumed successfully");
            return ExecutionOutput.builder().executionID(fuelString()).status(ExecutionStatus.SUCCESS).logs(logs).build();
        };
    }

    private String fuelString() {
        return fuelID + counter.getAndIncrement();
    }

    private boolean prime(int fuel) {
        for(int i = 2; i <= Math.sqrt(fuel); i++) {
            if(fuel % i == 0) {
                return false;
            }
        }
        return true;
    }
}
