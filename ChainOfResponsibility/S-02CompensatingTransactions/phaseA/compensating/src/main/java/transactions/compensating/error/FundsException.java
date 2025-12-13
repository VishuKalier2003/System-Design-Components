package transactions.compensating.error;

public class FundsException extends RuntimeException {
    public FundsException(String username, int amt) {
        super(username+" has lower amount value, thus for "+amt+" transaction cannot be processed");
    }
}
