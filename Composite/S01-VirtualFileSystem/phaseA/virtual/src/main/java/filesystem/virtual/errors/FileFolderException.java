package filesystem.virtual.errors;

public class FileFolderException extends RuntimeException {
    public FileFolderException(String path) {
        super("Error in creating or maintaining file/directory at path "+path);
    }

}
