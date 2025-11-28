package filesystem.virtual.middleware;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import filesystem.virtual.Enum.OperationStatus;
import filesystem.virtual.model.Adapter;
import lombok.Getter;

@Getter
@Component
public class MountRegistry {
    // should be a word not a path
    private final Map<String, Adapter> mountMap = new LinkedHashMap<>();

    @Autowired private CompositeRegistry compositeRegistry;

    public OperationStatus createMount(String nodeName, Adapter type) {
        if(mountMap.containsKey(nodeName))
            return OperationStatus.IDEMPOTENT;
        if(!compositeRegistry.existNode(nodeName))
            return OperationStatus.FAIL;
        mountMap.put(nodeName, type);
        return OperationStatus.DONE;
    }

    public OperationStatus updateMount(String nodeName, Adapter type) {
        if(!mountMap.containsKey(nodeName) || !compositeRegistry.existNode(nodeName))
            return OperationStatus.FAIL;
        mountMap.put(nodeName, type);
        return OperationStatus.DONE;
    }

    public Adapter getMount(String nodeName) {
        return mountMap.get(nodeName);
    }

    public boolean mountExists(String nodeName) {
        return mountMap.containsKey(nodeName);
    }
}
