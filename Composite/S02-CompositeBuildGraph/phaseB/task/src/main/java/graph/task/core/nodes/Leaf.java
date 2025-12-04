package graph.task.core.nodes;

import java.util.ArrayList;
import java.util.List;

import graph.task.data.output.ActionOutput;
import graph.task.model.Composite;

public class Leaf extends Composite {

    public Leaf(String name) {
        super(name);
    }

    @Override public List<Composite> getDependencies() {
        return new ArrayList<>();
    }

    @Override public boolean isLeaf() {return true;}

    @Override public List<ActionOutput> mergeOutputs() {
        return null;
    }
}
