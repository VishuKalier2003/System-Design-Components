package graph.task.enums.func;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import graph.task.data.context.ExecutionContext;
import graph.task.data.input.ActionInput;
import graph.task.data.output.ActionOutput;
import graph.task.enums.ActionStatus;
import graph.task.model.modules.Executable;
import graph.task.service.Cache;

// technique: enum + pluggable strategy where each enum stores a pluggable overridden function of an interface for dynamic use
public enum CacheFunction implements Executable {
    LRU_CACHE {
        @Override
        public BiFunction<ActionInput, ExecutionContext, ActionOutput> swappableFunction() {
            return (act, ctx) -> {
                Cache cache = ctx.getCache();
                List<String> log = new ArrayList<>();
                if(cache.contains(act.getReqID())) {
                    log.add("Cache hit, data was used recently");
                    return ActionOutput.builder().actionStatus(ActionStatus.CACHE_HIT).logs(log).build();
                } else {
                    log.add("Adding into Cache successfully");
                    ActionOutput output = ActionOutput.builder().actionStatus(ActionStatus.SUCCESS).logs(log).build();
                    cache.update(act.getReqID(), output);
                    return output;
                }
            };
        }
    };
}
