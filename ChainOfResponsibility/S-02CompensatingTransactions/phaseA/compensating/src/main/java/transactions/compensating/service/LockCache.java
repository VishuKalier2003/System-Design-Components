package transactions.compensating.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import transactions.compensating.enums.ResourceRequest;
import transactions.compensating.model.Resource;

@Service
public class LockCache implements Resource {
    // detail: using string and the key will be (username + bank), since a user can have multiple accounts
    private final Set<String> locked = new HashSet<>();

    // detail: for the sake of idempotency we are allowing a return value
    public synchronized boolean lock(String key) {
        if(isLocked(key))
            return false;
        locked.add(key);
        return true;
    }

    public synchronized boolean unlock(String key) {
        if(!isLocked(key))
            return false;
        locked.remove(key);
        return true;
    }

    public synchronized boolean isLocked(String key) {
        return locked.contains(key);
    }

    @Override
    public ResourceRequest getResourceType() {
        return ResourceRequest.LOCKER;
    }
}
