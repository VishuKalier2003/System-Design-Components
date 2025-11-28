package filesystem.virtual.errors;

public class NoTreeNodeException extends RuntimeException {
    public NoTreeNodeException(String data) {
        super("The Node does not exist along the path '"+data+"' in the vfs tree");
    }
}
