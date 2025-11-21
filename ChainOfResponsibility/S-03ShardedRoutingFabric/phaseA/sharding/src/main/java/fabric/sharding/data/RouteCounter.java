package fabric.sharding.data;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteCounter {
    private final Map<Double, Integer> routes = new HashMap<>();

    public void updateByOne(double key) {routes.put(key, routes.getOrDefault(key, 0) + 1);}
}
