package beyondcorp.google.error;

public class EntryNotExistException extends RuntimeException {
    public EntryNotExistException(String name) {
        super("The entry does not exist in Trust state origin "+name);
    }
}
