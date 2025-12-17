package beyondcorp.google.database;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import beyondcorp.google.store.User;

@Component
public class Database {
    private final Map<String, User> db = new HashMap<>();

    public void createEntry(User user) {db.put(user.getUuid(), user);}

    public User getUser(String key) {return db.get(key);}

    public boolean updateEntry(User user) {
        if(ifExist(user.getUuid())) {
            db.put(user.getUuid(), user);
            return true;
        }
        return false;
    }

    public boolean ifExist(String key) {return db.containsKey(key);}
}
