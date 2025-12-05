package graph.task.database;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import graph.task.model.Composite;
import lombok.Getter;
import lombok.Setter;

// Simple In-memory database
@Getter
@Setter
@Component
public class Database {
    private final ConcurrentHashMap<String, Composite> database = new ConcurrentHashMap<>();

    public void insert(String s, Composite c) {this.database.put(s, c);}

    public Composite get(String s) {return this.database.get(s);}

    public boolean contains(String s) {return database.containsKey(s);}
}
