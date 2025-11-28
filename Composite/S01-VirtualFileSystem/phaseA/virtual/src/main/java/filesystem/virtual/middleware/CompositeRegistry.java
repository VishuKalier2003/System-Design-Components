package filesystem.virtual.middleware;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import filesystem.virtual.model.Composite;
import lombok.Getter;

@Getter
@Component
public class CompositeRegistry {
    private final Map<String, Composite> nodeMap = new LinkedHashMap<>();
    private final Set<String> nodes = new LinkedHashSet<>();

    public boolean add(String path, Composite comp) {
        if(exist(path))
            return false;
        nodeMap.put(path, comp);
        nodes.add(comp.getNodeName());
        return true;
    }

    public boolean exist(String path) {return nodeMap.containsKey(path);}
    public boolean existNode(String node) {return nodes.contains(node);}

    public Composite get(String path) {
        if(!exist(path))
            return null;
        return nodeMap.get(path);
    }

    public boolean remove(String path) {
        if(!exist(path))
            return false;
        nodeMap.remove(path);
        nodes.remove(get(path).getNodeName());
        return true;
    }
}
