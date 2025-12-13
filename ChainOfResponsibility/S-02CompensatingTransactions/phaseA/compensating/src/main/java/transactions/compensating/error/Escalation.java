package transactions.compensating.error;

public class Escalation extends RuntimeException {
    public Escalation(String data) {
        super("Escalation happened due to exhausted retries from "+data);
    }
}
