package graph.task.data.context;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import graph.task.enums.ResourceRequest;
import graph.task.service.Cache;
import graph.task.service.Retry;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// detail: ExecutionContext created separately to not mingle data with resources
@Getter
@Setter
@Builder(toBuilder=true)
public class ExecutionContext {
    private Cache cache;
    private Retry retry;
    private Map<ResourceRequest, Supplier<Object>> resourceProvider;
    @Builder.Default
    private final AtomicInteger index = new AtomicInteger(0);
}
