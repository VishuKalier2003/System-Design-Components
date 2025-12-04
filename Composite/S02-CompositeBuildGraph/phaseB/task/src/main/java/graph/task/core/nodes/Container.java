package graph.task.core.nodes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import graph.task.data.output.ActionOutput;
import graph.task.model.Composite;

public class Container extends Composite {

    public Container(String name) {
        super(name);
    }

    @Override public List<Composite> getDependencies() {
        return new CopyOnWriteArrayList<>(dependencies);
    }

    @Override public boolean isLeaf() {return false;}

    @Override public List<ActionOutput> mergeOutputs() {
        return null;
    }
}
