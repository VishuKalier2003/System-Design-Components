package filesystem.virtual.errors;

public class PathException extends RuntimeException {
    public PathException(String path) {
        super("Error in path '"+path+"'");
    }
}
