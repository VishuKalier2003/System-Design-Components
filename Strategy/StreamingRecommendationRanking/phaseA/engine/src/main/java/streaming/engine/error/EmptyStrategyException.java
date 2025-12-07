package streaming.engine.error;

public class EmptyStrategyException extends RuntimeException {
    public EmptyStrategyException(String handler) {
        super("The Strategy for handler "+handler+" is not yet defined, set to Null");
    }
}
