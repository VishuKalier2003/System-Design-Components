package filesystem.virtual.service.admin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import filesystem.virtual.Enum.AdapterTypes;
import filesystem.virtual.Enum.OperationStatus;
import filesystem.virtual.errors.NoTreeNodeException;
import filesystem.virtual.errors.NomenclatureException;
import filesystem.virtual.middleware.MountRegistry;
import filesystem.virtual.model.Adapter;
import filesystem.virtual.service.MountExtractor;
import filesystem.virtual.utils.FileData;
import lombok.Getter;

@Service
public class FileManager {
    @Autowired private MountExtractor mountExtractor;
    @Autowired private MountRegistry mountRegistry;
    @Getter @Autowired @Qualifier("adapter_config") Map<AdapterTypes, StringBuilder> readMap;

    public OperationStatus createFolder(String path) {
        FileData fd = mountExtractor.extractPathDetails(path);
        try {
            if(fd.getRootName() == null)
                throw new NoTreeNodeException(path);
            return mountRegistry.getMount(fd.getRootName()).createFolder(fd.getRelativeFilePath());
        }
        catch(NoTreeNodeException e) {
            if(e.getCause() instanceof NoTreeNodeException)
                return OperationStatus.NOT_EXIST_ERROR;
            return OperationStatus.FAIL;
        }
    }

    public OperationStatus createFile(String path) {
        FileData fd = mountExtractor.extractPathDetails(path);
        try {
            if(fd.getRootName() == null)
                throw new NoTreeNodeException(path);
            OperationStatus opStatus = mountRegistry.getMount(fd.getRootName()).createFile(fd);
            if(opStatus == OperationStatus.NOMENCLATURE_ERROR)
                throw new NomenclatureException(path);
            return opStatus;
        } catch(NoTreeNodeException | NomenclatureException e) {
            if(e.getCause() instanceof NoTreeNodeException)
                return OperationStatus.NOT_EXIST_ERROR;
            else if(e.getCause() instanceof NomenclatureException)
                return OperationStatus.NOMENCLATURE_ERROR;
            return OperationStatus.FAIL;
        }
    }

    public OperationStatus readFile(String path) {
        FileData fd = mountExtractor.extractPathDetails(path);
        try {
            if(fd.getRootName() == null)
                throw new NoTreeNodeException(path);
            Adapter adapter = mountRegistry.getMount(fd.getRootName());
            adapter.readFile(fd);
            readMap.get(adapter.getTYPE()).append(adapter.getBUFFER().toString());
            adapter.getBUFFER().setLength(0);
            return OperationStatus.DONE;
        } catch(NoTreeNodeException e) {
            if(e.getCause() instanceof NoTreeNodeException)
                return OperationStatus.NOT_EXIST_ERROR;
            return OperationStatus.FAIL;
        }
    }

    public OperationStatus readDirectory(String path) {
        FileData fd = mountExtractor.extractPathDetails(path);
        try {
            if(fd.getRootName() == null)
                throw new NoTreeNodeException(path);
            Adapter adapter = mountRegistry.getMount(fd.getRootName());
            adapter.readFolder(fd);
            readMap.get(adapter.getTYPE()).append(adapter.getBUFFER().toString());
            adapter.getBUFFER().setLength(0);
            return OperationStatus.DONE;
        } catch(NoTreeNodeException e) {
            return OperationStatus.FAIL;
        }
    }

    public OperationStatus removePathOrDirectory(String path) {
        FileData fd = mountExtractor.extractPathDetails(path);
        try {
            if(fd.getRootName() == null)
                throw new NoTreeNodeException(path);
            mountRegistry.getMount(fd.getRootName()).remove(fd.getRelativeFilePath());
            // Clear mounted data
            clearMountedData(mountRegistry.getMount(fd.getRootName()).getTYPE().toString());
            return OperationStatus.DONE;
        } catch(NoTreeNodeException e) {
            return OperationStatus.FAIL;
        }
    }

    public OperationStatus writeFile(String path, String data) {
        FileData fd = mountExtractor.extractPathDetails(path);
        try {
            if(fd.getRootName() == null)
                throw new NoTreeNodeException(path);
            fd.setText(data);
            mountRegistry.getMount(fd.getRootName()).write(fd);
            return OperationStatus.DONE;
        } catch(NoTreeNodeException e) {
            return OperationStatus.FAIL;
        }
    }

    // Reading mounted data
    public String readMountedData(String root) {
        return readMap.get(AdapterTypes.valueOf(root.toUpperCase())).toString();
    }

    // When accessing mount registry always use "/"
    public void clearMountedData(String root) {
        readMap.get(AdapterTypes.valueOf(root.toUpperCase())).setLength(0);
    }
}
