package beyondcorp.google.model;

public interface Operation {
    public Actions operationConstant();

    public Object execute(Object input);
}
