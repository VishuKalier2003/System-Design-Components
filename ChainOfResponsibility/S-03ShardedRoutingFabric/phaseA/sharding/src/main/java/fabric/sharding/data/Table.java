package fabric.sharding.data;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Table {
    private int activeTime;
    private double lowerBoundary, upperBoundary, load;
    private Map<Double, Integer> channels;
}
