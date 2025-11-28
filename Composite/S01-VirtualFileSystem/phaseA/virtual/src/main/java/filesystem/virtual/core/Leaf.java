package filesystem.virtual.core;

import filesystem.virtual.errors.LeafHasNoChildException;
import filesystem.virtual.model.Composite;

public class Leaf extends Composite {

    public Leaf(String name) {
        super(name);
    }

    @Override public boolean isLeaf() {return true;}

    @Override public Composite getChild(String name) throws LeafHasNoChildException {
        throw new LeafHasNoChildException(this.nodeName);
    }
}
