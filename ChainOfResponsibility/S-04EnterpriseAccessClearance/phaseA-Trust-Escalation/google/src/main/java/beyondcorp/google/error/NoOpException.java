package beyondcorp.google.error;

public class NoOpException extends RuntimeException {
    public NoOpException(String name) {
        super("The operation does not exist for "+name);
    }
}
