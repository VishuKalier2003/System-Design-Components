package bus.notify_hub.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import bus.notify_hub.core.Observer;

@Component
public class Graph {
    private final Map<String, Set<Observer>> g = new HashMap<>();

    public synchronized void createVector(String newKey) {g.put(newKey, new HashSet<>());}

    public synchronized void insertObserver(String key, Observer ob) {g.get(key).add(ob);}

    public synchronized void removeObserver(String key, Observer ob) {g.get(key).remove(ob);}

    public Set<Observer> getActiveObservers(String key) {return g.get(key);}
}
