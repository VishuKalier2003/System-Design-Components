package streaming.engine.core.discovery;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import streaming.engine.data.output.Output;
import streaming.engine.database.Database;
import streaming.engine.enums.data.Genre;
import streaming.engine.model.Strategy;
import streaming.engine.service.GenreGraph;

@Component
public class ActivityStrategy {
    @Autowired private GenreGraph genreGraph;
    @Autowired private Database db;

    // technique: double abstraction as first the strategy() is called and then its underlying BiFunction(v1, v2)
    public final Strategy ACTIVITY_HIGH = () -> // first strategy
    (animeList, railType) -> {      // then underlying BiFunction, which can be used to fire its apply()
        Set<Genre> genres = animeList.getGenres().stream().collect(Collectors.toSet());
        Set<Genre> adjacentGenre = new HashSet<>();
        for(Genre genre : genres)
            adjacentGenre.addAll(genreGraph.nearGenres(genre, 2));
        return db.getAllAnime().stream().filter(x -> {
            for(Genre g : x.getGenre())
                if(adjacentGenre.contains(g))
                    return true;
            return false;
        }).map(x -> new Output(x, railType, x.getRating())).toList();
    };

    public final Strategy ACTIVITY_LOW = () -> // first strategy
    (animeList, railType) -> {      // then underlying BiFunction, which can be used to fire its apply()
        Set<Genre> genres = animeList.getGenres().stream().collect(Collectors.toSet());
        Set<Genre> adjacentGenre = new HashSet<>();
        for(Genre genre : genres)
            adjacentGenre.addAll(genreGraph.nearGenres(genre, 1));
        return db.getAllAnime().stream().filter(x -> {
            for(Genre g : x.getGenre())
                if(adjacentGenre.contains(g))
                    return true;
            return false;
        }).map(x -> new Output(x, railType, x.getRating())).toList();
    };
}
