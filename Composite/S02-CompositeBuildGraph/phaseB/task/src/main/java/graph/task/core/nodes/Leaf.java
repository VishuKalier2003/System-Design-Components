package graph.task.core.nodes;

import java.util.ArrayList;
import java.util.List;

import graph.task.model.Composite;

// technique: Composite pattern - (leaf nodes)
public class Leaf extends Composite {

    public Leaf(String name) {
        super(name);
    }

    @Override public List<Composite> getDependencies() {
        return new ArrayList<>();
    }

    @Override public boolean isLeaf() {return true;}

    // cannot add dependency to a leaf, hence returns `false`
    @Override public boolean addDependency(Composite node) {return false;}
}
