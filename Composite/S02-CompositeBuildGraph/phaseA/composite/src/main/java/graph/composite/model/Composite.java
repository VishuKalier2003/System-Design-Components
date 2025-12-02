package graph.composite.model;

import java.util.ArrayList;
import java.util.List;

import graph.composite.core.Action;

public abstract class Composite {
    protected final String name;
    protected final List<Composite> dependencies;

    protected Action action;

    public Composite(String name) {
        this.name = name;
        this.dependencies = new ArrayList<>();
    }

    public Action getAction() {return action;}
    public String getName() {return name;}
    public void setAction(Action action) {this.action = action;}

    public abstract List<Composite> getDependencies();
    public abstract boolean isLeaf();

    // Function for logging during tests
    public void show() {
        System.out.println("Composite Node Id : "+name);
        System.out.println("Action node : "+action);
        System.out.println("Is Leaf : "+isLeaf());
        System.out.println("Dependencies : "+dependencies.size());
    }
}
