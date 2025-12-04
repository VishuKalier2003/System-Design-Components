package graph.task.resources;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import graph.task.model.Resource;
import lombok.Setter;

@Setter
@Component
public class QuotaResource implements Resource<Integer> {
    private int lower = 100, upper = 999;

    @Override public Integer provide() {
        return ThreadLocalRandom.current().nextInt(lower, upper+1);
    }
}
