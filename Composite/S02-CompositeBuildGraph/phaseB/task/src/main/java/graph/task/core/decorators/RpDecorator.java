package graph.task.core.decorators;

import java.util.Map;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import graph.task.data.context.ExecutionContext;
import graph.task.enums.ResourceRequest;
import graph.task.model.Decorate;
import lombok.Setter;

// Resource Provider decorator
@Setter
@Component
public class RpDecorator implements Decorate {
    @Autowired @Qualifier("resource_provider") private Map<ResourceRequest, Supplier<Object>> provider;

    @Override public ExecutionContext enrich(ExecutionContext ctx) {
        return ctx.toBuilder().resourceProvider(provider).build();
    }
}
