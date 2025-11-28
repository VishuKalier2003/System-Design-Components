package filesystem.virtual.service.admin;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import filesystem.virtual.Enum.OperationStatus;
import filesystem.virtual.core.Container;
import filesystem.virtual.core.Leaf;
import filesystem.virtual.generator.PathGenerator;
import filesystem.virtual.middleware.CompositeRegistry;
import filesystem.virtual.model.Composite;

// Called and operated directly via APIs, hence a service
@Service
public class VfsManager {
    @Autowired private CompositeRegistry nodeMapper;
    @Autowired private PathGenerator pathGenerator;

    public Composite root;

    public boolean attachToRoot(String rootName) {
        if(root != null)
            return false;
        Container container = new Container(rootName);
        root = container;
        String path = pathGenerator.addPath(rootName);
        nodeMapper.add(path, root);
        return true;
    }

    public Composite getRoot() {return root;}

    public OperationStatus addNode(String homePath, String relativeName) {
        String path = pathGenerator.addRelativePath(homePath, relativeName);
        Composite node = nodeMapper.get(homePath), child = new Container(relativeName);
        if(node == null)
            return OperationStatus.PATH_ERROR;
        if(node.addChild(child) != OperationStatus.DONE)
            return OperationStatus.FAIL;
        nodeMapper.add(path, child);
        return OperationStatus.DONE;
    }

    public OperationStatus addLeaf(String homePath, String leafName) {
        String path = pathGenerator.addRelativePath(homePath, leafName);
        Composite node = nodeMapper.get(homePath), child = new Leaf(leafName);
        if(node == null)
            return OperationStatus.PATH_ERROR;
        if(node.addChild(child) != OperationStatus.DONE)
            return OperationStatus.FAIL;
        nodeMapper.add(path, child);
        return OperationStatus.DONE;
    }

    public Composite get(String path) {
        return nodeMapper.get(path);
    }

    public List<Composite> getChildren(String path) {
        return nodeMapper.get(path).getAll();
    }

    public Set<Composite> getSubtreeDfs(String path) {
        Set<Composite> subtree = new LinkedHashSet<>();
        dfs(path, subtree);
        return subtree;
    }

    private void dfs(String path, Set<Composite> subtree) {
        for(Composite comp : nodeMapper.get(path).getAll()) {
            if(comp != null) {
                subtree.add(comp);
                String newPath = pathGenerator.addRelativePath(path, comp.getNodeName());
                dfs(newPath, subtree);
            }
        }
    }
}
