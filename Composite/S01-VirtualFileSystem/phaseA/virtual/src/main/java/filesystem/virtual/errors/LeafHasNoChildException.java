package filesystem.virtual.errors;

public class LeafHasNoChildException extends RuntimeException {
    public LeafHasNoChildException(String word) {
        super("The Leaf with node name "+word+" cannot have any children");
    }
}
