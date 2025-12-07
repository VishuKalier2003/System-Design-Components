package streaming.engine.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import streaming.engine.data.Anime;
import streaming.engine.data.User;
import streaming.engine.data.input.Request;
import streaming.engine.database.Database;

@Service
public class Store {
    private final Map<String, Request> store = new HashMap<>();

    @Autowired
    private Database db;

    public boolean insert(String userID, User user) {
        if (store.containsKey(userID))
            return false;
        Request req = new Request();
        store.put(userID, req);
        for (String day : user.getActiveDays())
            insertDay(userID, day);
        // fixed: get the animeID, and then use it to get the genres (keep the conversions as simple as possible)
        for (String animeID : user.getWatchedIndex())
            insertGenre(userID, animeID);
        return true;
    }

    public void insertDay(String userID, String day) {
        store.get(userID).addActiveDay(day);
    }

    public void insertGenre(String userID, String animeID) {
        Request req = store.get(userID);
        Anime anime = db.getAnime(animeID);
        if (anime != null) {
            anime.getGenre().forEach(g -> req.getGenres().add(g));
        }
    }

    public boolean exist(String userID) {
        return store.containsKey(userID);
    }

    public Request getFromStore(String userID) {
        return store.get(userID);
    }
}
