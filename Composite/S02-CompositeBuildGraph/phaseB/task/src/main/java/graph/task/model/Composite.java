package graph.task.model;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import graph.task.model.modules.Executable;
import graph.task.model.modules.Preprocessing;

// technique: Composite pattern
public abstract class Composite {
    // This declares behavior
    protected final String nodeName;
    protected final Deque<Composite> dependencies;
    protected String fingerprint;

    // detail: No Engine attached here hence loose coupling
    protected Executable executionAction;
    protected Preprocessing preprocessingAction;

    public Composite(String name) {
        this.nodeName = name;
        this.dependencies = new ConcurrentLinkedDeque<>();
    }

    public void setExecution(Executable ex) {this.executionAction = ex;}
    public void setPreprocessing(Preprocessing pp) {this.preprocessingAction = pp;}

    public Executable execution() {return this.executionAction;}
    public Preprocessing preprocess() {return this.preprocessingAction;}

    public boolean isActiveExecutable() {return this.executionAction != null;}
    public boolean isActivePreprocess() {return this.preprocessingAction != null;}

    public String nodeName() {return this.nodeName;}

    public abstract List<Composite> getDependencies();
    public abstract boolean addDependency(Composite node);
    public abstract boolean isLeaf();
}
