package graph.composite.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import graph.composite.core.Action;
import graph.composite.dto.ExecutionOutput;
import graph.composite.dto.ManagerOutput;
import graph.composite.enums.ConfigType;
import graph.composite.enums.ExecutionStatus;
import graph.composite.enums.OutputStatus;
import graph.composite.model.Composite;
import graph.composite.utils.ConfigBlock;
import graph.composite.utils.Configuration;
import lombok.Setter;

@Setter
@Service
public class ExecutionManager {
    @Autowired private NodeManager nodeManager;
    @Autowired @Qualifier("action_factory") private Map<ConfigType, Function<ConfigBlock, ExecutionOutput>> registry;

    public ManagerOutput isGraphValid(String node) {
        Set<String> visited = new HashSet<>();
        Deque<Composite> q = new ArrayDeque<>();
        if(!nodeManager.containsNode(node)) {
            return ManagerOutput.builder().status(OutputStatus.ERROR_NOT_EXIST).log("The start node specified does not exist").build();
        }
        q.add(nodeManager.getNode(node));
        while(!q.isEmpty()) {
            Composite curr = q.poll();
            visited.add(node);
            for(Composite child : curr.getDependencies()) {
                if(visited.contains(child.getName())) {
                    return ManagerOutput.builder().status(OutputStatus.ERROR_CYCLIC_DEPENDENCY).log("graph contains cycle, cannot perform execution").build();
                }
                q.add(child);
            }
        }
        return ManagerOutput.builder().status(OutputStatus.PASS).log("graph is valid, can perform execution").build();
    }

    public List<ExecutionOutput> executeGraph(String node, Map<String, Configuration> configurations) {
        activateGraph(nodeManager.getNode(node), configurations);
        List<ExecutionOutput> outputs = new ArrayList<>();
        Deque<Composite> q = new ArrayDeque<>();
        q.add(nodeManager.getNode(node));
        while(!q.isEmpty()) {
            Composite curr = q.poll();
            Action act = curr.getAction();
            while(act.hasResources()) {
                ConfigType ct = act.fetchTopResourceType();
                if(ct == null) {
                    for(Composite child : curr.getDependencies())
                        q.add(child);
                }
                else {
                    act.setExecution(registry.get(ct));
                    ExecutionOutput eo = act.execute();
                    if(eo.getStatus() != ExecutionStatus.SUCCESS) {
                        outputs.add(ExecutionOutput.builder().executionID("terminated").build());
                        return outputs;
                    } else {
                        outputs.add(eo);
                    }
                }
            }
        }
        return outputs;
    }

    private boolean activateGraph(Composite node, Map<String, Configuration> configurations) {
        Deque<Composite> q = new ArrayDeque<>();
        q.add(node);
        while(!q.isEmpty()) {
            Composite curr = q.poll();
            if(configurations.containsKey(curr.getName()))
                curr.getAction().prepare(configurations.get(curr.getName()));
            for(Composite child : curr.getDependencies()) {
                q.add(child);
            }
        }
        return true;
    }
}
