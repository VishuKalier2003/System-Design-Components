package graph.composite.core.node;

import java.util.List;

import graph.composite.model.Composite;

public class Container extends Composite {

    public Container(String name) {
        super(name);
    }

    @Override public List<Composite> getDependencies() {return dependencies;}

    @Override public boolean isLeaf() {return false;}
}
