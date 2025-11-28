package filesystem.virtual.utils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FileData {
    // Filename with extensions
    private String fileName;
    // Root name as the node which is the mounting anchor
    private String rootName;
    // relative path of file from root HOME
    private String relativeFilePath;
    // content of the file
    private String text;
}
