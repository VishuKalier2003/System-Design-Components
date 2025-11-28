package filesystem.virtual.core;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import filesystem.virtual.Enum.OperationStatus;
import filesystem.virtual.model.Composite;

public class Container extends Composite {
    private final Map<String, Composite> children;

    public Container(String name) {
        super(name);
        this.children = new LinkedHashMap<>();
    }

    @Override public boolean isLeaf() {return false;}

    @Override public OperationStatus addChild(Composite child) {
        String c = child.getNodeName();
        if(children.containsKey(c))
            return OperationStatus.FAIL;
        children.put(c, child);
        return OperationStatus.DONE;
    }

    @Override public OperationStatus removeChild(Composite child) {
        String c = child.getNodeName();
        if(!children.containsKey(c))
            return OperationStatus.FAIL;
        children.remove(c);
        return OperationStatus.DONE;
    }

    @Override public Composite getChild(String childName) {return children.get(childName);}

    @Override public List<Composite> getAll() {return children.values().stream().toList();}
}
