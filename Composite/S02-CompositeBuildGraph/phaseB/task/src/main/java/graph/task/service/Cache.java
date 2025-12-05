package graph.task.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;

import graph.task.data.output.ActionOutput;

// detail: A small cache unit
@Service
public class Cache {
    // A small LRU Cache of definite size
    private final int maxSize = 5;
    private final ActionOutput[] cache = new ActionOutput[maxSize];
    private final AtomicInteger index = new AtomicInteger(-1);      // Integer for atomic operations
    private final ConcurrentHashMap<String, Integer> mp = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public boolean update(String reqID, ActionOutput data) {
        try {
            lock.lock();
            if(index.intValue() == maxSize-1) {
                index.set(-1);
            }
            cache[index.incrementAndGet()] = data;
            mp.put(reqID, index.intValue());
            return true;
        } catch(NullPointerException e) {
            System.out.println("NULL POINTER EXCEPTION HIT AT CACHE INDEX "+index.intValue());
            return false;
        } finally {
            lock.unlock();
        }
    }

    public boolean contains(String reqID) {
        return mp.containsKey(reqID);
    }

    public ActionOutput get(String reqID) {
        if(!mp.containsKey(reqID))
            return null;
        else {
            if(index.intValue() == -1)
                return null;
            return cache[mp.get(reqID)];
        }
    }
}
