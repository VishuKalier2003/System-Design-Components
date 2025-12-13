package transactions.compensating.error;

public class NotInDatabaseException extends RuntimeException {
    public NotInDatabaseException(String name, String receiverOrSender) {
        super("The User "+name+" who is currently "+receiverOrSender+" does not exist in the specified DB");
    }
}
