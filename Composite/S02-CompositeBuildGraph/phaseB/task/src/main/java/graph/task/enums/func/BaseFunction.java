package graph.task.enums.func;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import graph.task.data.context.ExecutionContext;
import graph.task.data.input.ActionInput;
import graph.task.data.output.ActionOutput;
import graph.task.enums.ActionStatus;
import graph.task.enums.ResourceRequest;
import graph.task.model.modules.Executable;

public enum BaseFunction implements Executable {

    BASE_METAL_PURE {
        @Override
        public BiFunction<ActionInput, ExecutionContext, ActionOutput> swappableFunction() {
            return (action, context) -> {
                List<String> logs = new ArrayList<>();
                Map<ResourceRequest, Supplier<Object>> provider = context.getResourceProvider();
                while(action.hasResource()) {
                    String resource = action.currentResource();
                    ResourceRequest req = ResourceRequest.valueOf(resource.toUpperCase());
                    var data = provider.get(req);
                    if(data != null) {
                        switch(req) {
                            case QUOTA -> {
                                logs.add("QUOTA of value "+(int)(data.get())+" added to "+action.getReqID());
                            }
                            case TOKEN -> {
                                logs.add("TOKEN "+(int)(data.get())+" provided to "+action.getReqID());
                            }
                            case FLAG -> {
                                logs.add("FLAG value "+(int)(data.get())+" given to "+action.getReqID());
                            }
                            default -> {
                                logs.add("RESOURCE ASKED CANNOT BE GIVEN BY THE PROVIDER");
                                return ActionOutput.builder().actionStatus(ActionStatus.REQUEST_ERROR).logs(logs).code(-1).build();
                            }
                        }
                    } else return ActionOutput.builder().actionStatus(ActionStatus.REQUEST_ERROR).logs(logs).code(-1).build();
                    action.moveIndexForward();
                }
                return ActionOutput.builder().actionStatus(ActionStatus.SUCCESS).logs(logs).code(100).build();
            };
        }
    },

    // This skips the resources at odd indices (hence for simplicity termed as impure)
    BASE_METAL_IMPURE {
        @Override
        public BiFunction<ActionInput, ExecutionContext, ActionOutput> swappableFunction() {
            return (action, context) -> {
                List<String> logs = new ArrayList<>();
                Map<ResourceRequest, Supplier<Object>> provider = context.getResourceProvider();
                while(action.hasResource()) {
                    String resource = action.currentResource();
                    ResourceRequest req = ResourceRequest.valueOf(resource.toUpperCase());
                    var data = provider.get(req);
                    if(data != null) {
                        switch(req) {
                            case QUOTA -> {
                                logs.add("QUOTA of value "+(int)(data.get())+" added to "+action.getReqID());
                            }
                            case TOKEN -> {
                                logs.add("TOKEN "+(int)(data.get())+" provided to "+action.getReqID());
                            }
                            case FLAG -> {
                                logs.add("FLAG value "+(int)(data.get())+" given to "+action.getReqID());
                            }
                            default -> {
                                logs.add("RESOURCE ASKED CANNOT BE GIVEN BY THE PROVIDER");
                                return ActionOutput.builder().actionStatus(ActionStatus.REQUEST_ERROR).logs(logs).code(-1).build();
                            }
                        }
                    } else return ActionOutput.builder().actionStatus(ActionStatus.REQUEST_ERROR).logs(logs).code(-1).build();
                    action.moveIndexForward();
                    action.moveIndexForward();
                }
                return ActionOutput.builder().actionStatus(ActionStatus.SUCCESS).logs(logs).code(100).build();
            };
        }
    }
}
