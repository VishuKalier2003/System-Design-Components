package filesystem.virtual.model;

import java.util.ArrayList;
import java.util.List;

import filesystem.virtual.Enum.OperationStatus;
import filesystem.virtual.errors.LeafHasNoChildException;

// Need map or registry of path to Composite (used to extract the node) and path to adapter
public abstract class Composite {
    protected final String nodeName;

    public Composite(String node) {
        this.nodeName = node;
    }

    public abstract boolean isLeaf();
    public abstract Composite getChild(String name) throws LeafHasNoChildException;

    public String getNodeName() {return nodeName;}

    // If not overridden, they are automatically inherited as it is (in case of Leaf node)
    public OperationStatus addChild(Composite child) {return OperationStatus.DONE;}
    public OperationStatus removeChild(Composite child) {return OperationStatus.DONE;}
    public List<Composite> getAll() {return new ArrayList<>();}
}
