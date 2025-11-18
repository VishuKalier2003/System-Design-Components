package distributed.saga.data;

public enum CommandState {
    RECEIVE, COMPENSATE, EXTRACT, EXECUTE;
}
