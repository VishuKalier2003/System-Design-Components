package transactions.compensating.error;

public class IdempotencyException extends RuntimeException {
    public IdempotencyException(String resource, String ID) {
        super("Idempotency error caused in "+resource+" by handler "+ID);
    }
}
