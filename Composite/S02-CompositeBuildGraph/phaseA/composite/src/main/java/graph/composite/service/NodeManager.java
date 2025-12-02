package graph.composite.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import graph.composite.dto.ManagerOutput;
import graph.composite.enums.OutputStatus;
import graph.composite.middleware.NodeRegistry;
import graph.composite.model.Composite;

@Service
public class NodeManager {
    @Autowired private NodeRegistry registry;
    private Composite root;

    public boolean attachRoot(Composite node) {
        if(root == null) {
            this.root = node;
            registry.registerNode(node.getName(), node);
            return true;
        }
        return false;
    }

    public boolean checkRoot() {return root != null;}

    public Composite getRoot() {return root;}

    public ManagerOutput attachContainerOrLeaf(String parent, Composite node) {
        Composite pa = registry.getNode(parent);
        if(pa.isLeaf())
            return ManagerOutput.builder().status(OutputStatus.ERROR_LEAF_EXTEND).log("Error for attaching to a leaf").build();
        if(registry.containsNode(node.getName()))
            return ManagerOutput.builder().status(OutputStatus.ERROR_DUPLICATE_EXIST).log("Node ID already exists").build();
        pa.getDependencies().add(node);
        registry.registerNode(node.getName(), node);
        return ManagerOutput.builder().status(OutputStatus.PASS).log("Passed").build();
    }

    public List<Composite> getDependencies(String id) {
        return registry.getNode(id).getDependencies();
    }

    public boolean containsDependency(String id) {
        return registry.containsNode(id) && !registry.getNode(id).isLeaf();
    }

    public Composite getNode(String id) {
        return registry.getNode(id);
    }

    public boolean containsNode(String node) {
        return registry.containsNode(node);
    }

    public List<Composite> bfs() {
        List<Composite> res = new ArrayList<>();
        Deque<Composite> queue = new ArrayDeque<>();
        queue.add(root);
        while(!queue.isEmpty()) {
            Composite curr = queue.poll();
            res.add(curr);
            for(Composite child : curr.getDependencies())
                queue.add(child);
        }
        return res;
    }
}
