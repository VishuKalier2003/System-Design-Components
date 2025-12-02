package graph.composite.core.node;

import java.util.ArrayList;
import java.util.List;

import graph.composite.model.Composite;

public class Leaf extends Composite {

    public Leaf(String name) {
        super(name);
    }

    @Override public List<Composite> getDependencies() {return new ArrayList<>();}

    @Override public boolean isLeaf() {return true;}
}
