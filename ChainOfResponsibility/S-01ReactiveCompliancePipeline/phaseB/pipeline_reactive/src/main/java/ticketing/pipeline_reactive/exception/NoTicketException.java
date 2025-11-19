package ticketing.pipeline_reactive.exception;

public class NoTicketException extends RuntimeException {
    public NoTicketException(String data) {
        super("Currently there are no tickets of Quota "+data);
    }
}
