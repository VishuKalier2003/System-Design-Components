package graph.task.core.decorators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import graph.task.data.context.ExecutionContext;
import graph.task.model.Decorate;
import graph.task.service.Retry;
import lombok.Getter;

@Getter
@Component
public class RetryDecorator implements Decorate {
    @Autowired private Retry retry;     // retry logic wired

    @Override public ExecutionContext enrich(ExecutionContext ctx) {
        return ctx.toBuilder().retry(retry).build();
    }
}
