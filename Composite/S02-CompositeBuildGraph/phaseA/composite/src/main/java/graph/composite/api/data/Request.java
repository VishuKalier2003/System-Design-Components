package graph.composite.api.data;

import graph.composite.utils.Configuration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Request {
    private String node;
    private Configuration data;
}
