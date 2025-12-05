package graph.task.utils;

import java.util.List;

import org.springframework.stereotype.Component;

import graph.task.core.nodes.Container;
import graph.task.core.nodes.Leaf;
import graph.task.data.input.ActionInput;
import graph.task.data.input.Rule;
import graph.task.data.input.Rule.NodeInput;
import graph.task.model.Composite;

// technique: Factory - creates object only
@Component
public class Factory {

    public Composite createNode(String name, boolean leaf) {
        return leaf ? new Leaf(name) : new Container(name);
    }

    public ActionInput create(Rule rule, NodeInput nodeInput) {
        if (rule == null || nodeInput == null)
            throw new IllegalArgumentException("Rule and NodeInput cannot be null");
        List<String> resources = nodeInput.getResources();
        // Default values
        boolean retryFlag = false;
        int retryCount = 0;
        // Retry feature parsing only if present
        if (nodeInput.getFeatures() != null && nodeInput.getFeatures().containsKey("retry")) {
            retryFlag = true;
            retryCount = parseRetryCount(nodeInput.getFeatures().get("retryCount"));
        }
        return ActionInput.builder()
                .reqID(rule.getRuleID())                    // Execution anchor
                .actions(resources)                         // Work units to execute
                .retryFlag(retryFlag)                       // default false
                .retryCount(retryCount)                     // default 0
                .build();
    }

    // Fallback-safe retryCount parser
    private int parseRetryCount(String countValue) {
        try {
            return (countValue != null) ? Integer.parseInt(countValue) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
