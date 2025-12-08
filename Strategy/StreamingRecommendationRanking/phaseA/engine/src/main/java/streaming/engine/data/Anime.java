package streaming.engine.data;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import streaming.engine.enums.data.AiringDay;
import streaming.engine.enums.data.Genre;

@Setter
@Getter
public class Anime {
    private Set<Genre> genre;
    private String name, Id;
    private Set<AiringDay> days;
    private int rating;

    // fixed: double-check whether the constructors have filled the data properly or not
    public Anime(String name, String Id, int rating, String day[], String g[]) {
        this.name = name;
        this.Id = Id;
        this.rating = rating;
        this.genre = new HashSet<>();
        for(String gen : g)
            this.genre.add(Genre.valueOf(gen.toUpperCase()));
        this.days = new HashSet<>();
        for(String d : day)
            this.days.add(AiringDay.valueOf(d.toUpperCase()));
    }

    public void show() {
        System.out.println("Anime Id : "+Id+" anime name : "+name);
        System.out.println("Anime Genre : "+genre);
        System.out.println("Anime watch days : "+days);
        System.out.println("Anime rating : "+rating);
    }
}
