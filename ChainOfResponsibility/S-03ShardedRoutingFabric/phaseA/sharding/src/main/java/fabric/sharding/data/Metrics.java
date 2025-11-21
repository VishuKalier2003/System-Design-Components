package fabric.sharding.data;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Metrics {
    private final AtomicInteger countTenants = new AtomicInteger();
    private double loadValue = 0.0d;

    public void increaseActivity(int value) {
        countTenants.addAndGet(value);
    }

    public void increaseLoad(double amount) {
        loadValue += amount;
    }
}
