package fabric.sharding.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteRequest {
    private String shard;
    private double updated;
}
