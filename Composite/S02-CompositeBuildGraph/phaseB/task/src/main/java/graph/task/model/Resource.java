package graph.task.model;

// technique: Generic Output Interface - concrete classes produce outputs of different data types, yet can be safely type checked
public interface Resource<T> {
    public T provide();     // The Generic T here can be safely down-casted
}
