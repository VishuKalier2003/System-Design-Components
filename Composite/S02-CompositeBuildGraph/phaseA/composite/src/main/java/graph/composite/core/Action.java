package graph.composite.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import graph.composite.core.resource.FuelResource;
import graph.composite.core.resource.QuotaResource;
import graph.composite.core.resource.TokenResource;
import graph.composite.dto.ExecutionOutput;
import graph.composite.enums.ConfigType;
import graph.composite.enums.ExecutionStatus;
import graph.composite.model.Resource;
import graph.composite.utils.ConfigBlock;
import graph.composite.utils.Configuration;
import lombok.Setter;

@Setter
@Scope("prototype")
@Component
public class Action {
    @Autowired @Qualifier("resources") private Map<ConfigType, Resource> resources;

    protected List<ConfigBlock> allocatedResources;
    protected Function<ConfigBlock, ExecutionOutput> function;

    private final AtomicInteger index = new AtomicInteger(0);

    public void prepare(Configuration config) {
        List<ConfigBlock> queue = new ArrayList<>();
        for (var entry : config.getConfigs().entrySet()) {
            var type = entry.getKey();
            var val = entry.getValue();
            var res = resources.get(type);
            switch (type) {
                case QUOTA -> {
                    var q = (QuotaResource) res;
                    while (!q.checkResource())
                        q.requestResource();
                    queue.add(ConfigBlock.builder().configType(type).value(val).resource(q.assignQuota(val)).build());
                }
                case FUEL -> {
                    var f = (FuelResource) res;
                    while (!f.checkResource())
                        f.requestResource();
                    queue.add(ConfigBlock.builder().configType(type).value(val).resource(f.assignFuel(val)).build());
                }
                case TOKEN -> {
                    var t = (TokenResource) res;
                    while (!t.checkResource())
                        t.requestResource();
                    queue.add(ConfigBlock.builder().configType(type).value(val).resource(t.acquireToken()).build());
                }
                default -> throw new IllegalArgumentException("Unknown resource type");
            }
        }
        this.allocatedResources = queue;
    }

    public void setExecution(Function<ConfigBlock, ExecutionOutput> fn) {this.function = fn;}

    public ConfigBlock fetchTopResource() {
        int idx = index.getAndIncrement();
        return allocatedResources.get(idx);
    }

    public boolean hasResources() {return index.intValue() < allocatedResources.size();}

    public ConfigType fetchTopResourceType() {
        if(hasResources())
            return allocatedResources.get(index.intValue()).getConfigType();
        return null;
    }

    public ExecutionOutput execute() {
        if(hasResources())
            return function.apply(fetchTopResource());
        return ExecutionOutput.builder().status(ExecutionStatus.NO_RESOURCE).build();
    }
}
