package graph.task.enums.func;

import java.util.function.BiFunction;

import graph.task.data.context.ExecutionContext;
import graph.task.data.input.ActionInput;
import graph.task.model.modules.Preprocessing;

// technique: enum + pluggable strategy where each enum stores a pluggable overridden function of an interface for dynamic use
public enum RetryFunction implements Preprocessing {
    HALF {      // detail: enum object hence singleton source of call, need not have explicit object factory to produce them
        @Override
        public BiFunction<ActionInput, ExecutionContext, ActionInput> preprocessFunction() {
            return (act, ctx) -> {
                return ctx.getRetry().resetHalf(act);
            };
        }
    },
    FULL {
        @Override
        public BiFunction<ActionInput, ExecutionContext, ActionInput> preprocessFunction() {
            return (act, ctx) -> {
                return ctx.getRetry().resetFull(act);
            };
        }
    },
    QUARTER {
        @Override
        public BiFunction<ActionInput, ExecutionContext, ActionInput> preprocessFunction() {
            return (act, ctx) -> {
                return ctx.getRetry().resetQuarter(act);
            };
        }
    }
}
