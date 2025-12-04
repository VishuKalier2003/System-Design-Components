package graph.task.enums.func;

import java.util.function.BiFunction;

import graph.task.data.context.ExecutionContext;
import graph.task.data.input.ActionInput;
import graph.task.model.modules.Preprocessing;

public enum RetryFunction implements Preprocessing {
    HALF {
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
