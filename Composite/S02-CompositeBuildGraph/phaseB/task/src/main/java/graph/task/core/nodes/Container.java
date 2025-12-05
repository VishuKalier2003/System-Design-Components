package graph.task.core.nodes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import graph.task.model.Composite;

// technique: Composite pattern - container nodes (non-leaf nodes)
public class Container extends Composite {

    public Container(String name) {
        super(name);
    }

    @Override public List<Composite> getDependencies() {
        return new CopyOnWriteArrayList<>(dependencies);
    }

    @Override public boolean isLeaf() {return false;}

    @Override public boolean addDependency(Composite node) {
        dependencies.add(node);     // adds the node as child
        return true;
    }
}
