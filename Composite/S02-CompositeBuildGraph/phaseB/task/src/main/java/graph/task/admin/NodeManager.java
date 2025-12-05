package graph.task.admin;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import graph.task.data.input.Rule;
import graph.task.database.Database;
import graph.task.model.Composite;
import graph.task.utils.Factory;

@Service
public class NodeManager {
    @Autowired private Database database;
    @Autowired private Factory factory;

    private final ReentrantLock lock = new ReentrantLock();
    private final Logger log = LoggerFactory.getLogger(NodeManager.class);

    // This functions ensures that the prepareRule is deterministic and thread-safe
    public void prepareRule(Rule rule) {
        // technique: locking using try-catch-finally block
        try {
            lock.lock();
            String pa = rule.getParent();
            parentOperation(pa);
            childOperation(pa, rule.getDependents());
        } catch(Exception e) {}
        finally {
            lock.unlock();
        }
    }

    public List<Composite> bfs(String root) {
        Deque<Composite> q = new ArrayDeque<>();
        List<Composite> lst = new ArrayList<>();
        q.add(database.get(root));
        while(!q.isEmpty()) {
            Composite node = q.poll();
            lst.add(node);
            for(Composite neighbor : node.getDependencies()) {
                q.add(neighbor);
            }
        }
        return lst;
    }

    private void parentOperation(String pa) {
        if(!database.contains(pa)) {
            Composite node = factory.createNode(pa, false);
            database.insert(pa, node);
        }
        log.info("Parent Operation completed");
    }

    private void childOperation(String pa, List<String> children) {
        Composite parent = database.get(pa);
        for(String childName : children) {
            Composite node = factory.createNode(childName, false);
            if(!database.contains(childName))
                database.insert(childName, node);
            parent.addDependency(node);
        }
        log.info("Child Operation completed");
    }
}
