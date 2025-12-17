package beyondcorp.google.error;

public class InvalidationException extends RuntimeException {
    public InvalidationException(String name) {
        super("The token is invalidated for the operation "+name);
    }
}
