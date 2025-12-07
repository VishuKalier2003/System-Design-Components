package streaming.engine.database;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import streaming.engine.data.Anime;
import streaming.engine.utils.UUIDgenerator;

@Component
public class Database {
    private final Map<String, Anime> db = new LinkedHashMap<>();
    @Autowired private UUIDgenerator uuid;

    @PostConstruct
    public void init() {
        db.put("anime-1", create("My Hero Academia", 8, new String[]{"thursday", "friday", "sunday"}, new String[]{"kids", "adventure", "school", "action"}));
        db.put("anime-2", create("The Eminence in Shadow", 7, new String[]{"monday", "friday"}, new String[]{"fantasy", "drama", "isekai"}));
        db.put("anime-3", create("Demon Slayer", 10, new String[]{"saturday"}, new String[]{"fantasy", "adventure", "action"}));
        db.put("anime-4", create("Doraemon", 8, new String[]{"monday", "tuesday", "wednesday", "thursday","saturday", "friday", "sunday"}, new String[]{"kids", "soft", "fantasy", "drama"}));
        db.put("anime-5", create("Jujutsu Kaisen", 8, new String[]{"wednesday", "friday"}, new String[]{"gorr", "action", "thrill"}));
        db.put("anime-6", create("Your Lie In April", 7, new String[]{"tuesday", "saturday"}, new String[]{"love", "soft"}));
        db.put("anime-7", create("Tokyo Ghoul", 7, new String[]{"thursday", "friday", "sunday"}, new String[]{"gorr", "thrill", "murder"}));
        db.put("anime-8", create("Monster", 9, new String[]{"thursday", "sunday"}, new String[]{"murder", "thrill", "psychological"}));
        db.put("anime-9", create("Classroom of the Elite", 10, new String[]{"wednesday", "sunday"}, new String[]{"school", "psychological"}));
        db.put("anime-10", create("Higurashi: When They Cry", 10, new String[]{"friday", "thursday", "monday"}, new String[]{"psychological", "gorr", "murder", "fantasy"}));
        db.put("anime-11", create("Fairy Tail", 7, new String[]{"tuesday", "wednesday"}, new String[]{"kids", "adventure", "thrill", "action"}));
        db.put("anime-12", create("Fate Stay Night", 9, new String[]{"monday", "wednesday"}, new String[]{"action", "adventure", "thrill", "fantasy"}));
        db.put("anime-13", create("Spy Family", 9, new String[]{"monday", "saturday"}, new String[]{"kids", "soft", "drama", "school"}));
        db.put("anime-14", create("Kakegurui", 7, new String[]{"wednesday"}, new String[]{"thrill", "psychological", "school"}));
        db.put("anime-15", create("Goblin Slayer", 7, new String[]{"sunday"}, new String[]{"thrill", "action", "adventure"}));
        db.put("anime-16", create("Another", 6, new String[]{"monday", "thursday"}, new String[]{"murder", "psychological", "school"}));
        db.put("anime-17", create("Pokemon", 7, new String[]{"monday", "tuesday", "wednesday", "thursday","saturday", "friday", "sunday"}, new String[]{"kids", "soft", "fantasy", "adventure", "action"}));
        db.put("anime-18", create("Attack on Titan", 8, new String[]{"monday", "tuesday", "wednesday", "thursday","saturday", "friday", "sunday"}, new String[]{"psychological", "murder", "action", "fantasy"}));
        db.put("anime-19", create("Summertime rendering", 10, new String[]{"tuesday", "wednesday", "friday", "sunday"}, new String[]{"psychological", "gorr", "fantasy", "action"}));
        db.put("anime-20", create("Bleach", 9, new String[]{"saturday", "friday", "sunday"}, new String[]{"action", "adventure", "drama"}));
        db.put("anime-21", create("Highschool of the Dead", 5, new String[]{"friday", "sunday"}, new String[]{"murder", "action", "drama"}));
    }

    private Anime create(String name, int rating, String days[], String genres[]) {
        return new Anime(name, uuid.uuid(), rating, days, genres);
    }

    public List<Anime> getAllAnime() {return db.values().stream().toList();}

    public Anime getAnime(String Id) {return db.get(Id);}

    public Map<String, Anime> getAll() {return db;}
}
