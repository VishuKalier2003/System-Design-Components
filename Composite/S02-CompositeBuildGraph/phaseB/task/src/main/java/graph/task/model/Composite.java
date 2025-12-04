package graph.task.model;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import graph.task.data.output.ActionOutput;

public abstract class Composite {
    protected final String nodeName;
    protected final Deque<Composite> dependencies;
    protected String fingerprint;

    public Composite(String name) {
        this.nodeName = name;
        this.dependencies = new ConcurrentLinkedDeque<>();
    }

    public abstract List<Composite> getDependencies();
    public abstract boolean isLeaf();
    public abstract List<ActionOutput> mergeOutputs();
}
