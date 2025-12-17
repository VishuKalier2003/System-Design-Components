package beyondcorp.google.service.func;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import beyondcorp.google.database.Database;
import beyondcorp.google.store.User;

@Service
public class UserProfile {
    @Autowired private Database db;

    public boolean createUser(User user) {
        if(db.ifExist(user.getUuid()))
            return false;
        db.createEntry(user);
        return true;
    }

    public User getUserData(String uuid) {
        return db.getUser(uuid);
    }

    public User updateUser(User user) {
        db.updateEntry(user);
        return user;
    }
}
