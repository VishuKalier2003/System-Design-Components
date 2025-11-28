package filesystem.virtual.model;

import filesystem.virtual.Enum.AdapterTypes;
import filesystem.virtual.Enum.OperationStatus;
import filesystem.virtual.utils.FileData;
import lombok.Getter;

@Getter
public abstract class Adapter {
    protected final String HOME, NAME;
    protected final AdapterTypes TYPE;
    protected final StringBuilder BUFFER;

    public Adapter(String relativePath, AdapterTypes type, String name) {
        this.HOME = relativePath;
        this.TYPE = type;
        this.NAME = name;
        this.BUFFER = new StringBuilder();
    }

    public abstract OperationStatus createFolder(String path);
    public abstract OperationStatus createFile(FileData fd);
    public abstract OperationStatus readFile(FileData fd);
    public abstract OperationStatus readFolder(FileData fd);
    public abstract OperationStatus write(FileData fd);
    public abstract OperationStatus remove(String path);
}
