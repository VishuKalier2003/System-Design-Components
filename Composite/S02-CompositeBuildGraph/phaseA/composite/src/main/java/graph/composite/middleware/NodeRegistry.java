package graph.composite.middleware;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import graph.composite.model.Composite;

@Component
public class NodeRegistry {
    private final Map<String, Composite> registry = new HashMap<>();

    public synchronized boolean registerNode(String id, Composite node) {
        if (!registry.containsKey(id)) {
            registry.put(id, node);
            return true;
        }
        return false;
    }

    public synchronized Composite getNode(String id) {
        return registry.get(id);
    }

    public synchronized boolean containsNode(String id) {
        return registry.containsKey(id);
    }
}
