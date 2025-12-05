package graph.task.core.decorators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import graph.task.data.context.ExecutionContext;
import graph.task.model.Decorate;
import graph.task.service.Cache;
import lombok.Getter;

// Created as a singleton component since it does not store data rather behaves as a supplier
@Getter
@Component
public class CacheDecorator implements Decorate {
    @Autowired private Cache cache;

    @Override public ExecutionContext enrich(ExecutionContext ctx) {
        return ctx.toBuilder().cache(cache).build();
    }
}
