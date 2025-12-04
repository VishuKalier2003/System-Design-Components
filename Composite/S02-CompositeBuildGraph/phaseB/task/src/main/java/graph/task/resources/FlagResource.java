package graph.task.resources;

import org.springframework.stereotype.Component;

import graph.task.model.Resource;
import lombok.Setter;

@Setter
@Component
public class FlagResource implements Resource<Boolean> {
    private double threshold = 0.5d;

    @Override public Boolean provide() {
        return Math.random() < threshold;
    }
}
