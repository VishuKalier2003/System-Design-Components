package filesystem.virtual.service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import filesystem.virtual.Enum.OperationStatus;
import filesystem.virtual.middleware.MountRegistry;
import filesystem.virtual.utils.FileData;

@Service
public class MountExtractor {
    @Autowired private MountRegistry mountRegistry;

    // A path will contain only one adapter, if one wants to add multiple adapters, one can unmount the ancestor node
    public FileData extractPathDetails(String path) {
        String nodes[] = path.split("/");
        int n = nodes.length;
        String nx = null, file = null;
        StringBuilder p = new StringBuilder();
        for(int i = 0; i < n; i++) {
            String node = nodes[i];
            if(mountRegistry.mountExists(node)) {
                nx = node;
                for(int j = i+1; j < n-1; j++) {
                    p.append(nodes[j]).append("/");
                }
                if(nodes[n-1].contains("."))
                    file = nodes[n-1];
                else
                    p.append(nodes[n-1]);
            }
        }
        // Avoiding NPE as empty String builder would hit NPE when converted to string
        if(p.isEmpty())
            p.append("");
        return FileData.builder().fileName(file).rootName(nx).relativeFilePath(p.toString()).build();
    }

    public OperationStatus provideText(FileData fd, String text) {
        if(fd.getText().equals(text))
            return OperationStatus.IDEMPOTENT;
        fd.setText(text);
        return OperationStatus.DONE;
    }

    public Set<String> extractStrings(String path) {
        return Arrays.stream(path.split("/")).collect(Collectors.toSet());
    }
}
