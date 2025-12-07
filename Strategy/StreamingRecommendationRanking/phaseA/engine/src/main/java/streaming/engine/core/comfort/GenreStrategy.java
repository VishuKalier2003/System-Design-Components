package streaming.engine.core.comfort;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import streaming.engine.data.output.Output;
import streaming.engine.database.Database;
import streaming.engine.enums.data.Genre;
import streaming.engine.model.Strategy;

@Component
public class GenreStrategy {
    @Autowired private Database database;

    // technique: nested lambda expression to call the interface function and execute the inner function as well
    public final Strategy MAX_GENRE = () ->     // The strategy() function has no input so stateless lambda
    (animeList, railType) -> {      // The BiFunction has two inputs, hence two state lambda
        Map<Genre, Integer> fMap = new EnumMap<>(Genre.class);
        for(Genre genre : animeList.getGenres())
            fMap.put(genre, fMap.getOrDefault(genre, 0) + 1);
        Genre max[] = new Genre[]{Genre.ACTION};
        int maxValue = 0;
        for(Map.Entry<Genre, Integer> e : fMap.entrySet())
            if(e.getValue() > maxValue) {
                max[0] = e.getKey();
                maxValue = e.getValue();
            }
        return database.getAllAnime()
            .stream().filter(x -> x.getGenre().contains(max[0]))
            .map(x -> new Output(x, railType))
            .toList();
    };

    // detail: needs to be invoked as GenreStrategy.GENRE_FOR_TWIN.strategy().apply(v1, v2) to get result as List<Output>
    public final Strategy GENRE_FOR_TWIN = () -> (animeList, railType) -> {
        Map<Genre, Integer> fMap = new EnumMap<>(Genre.class);
        for(Genre genre : animeList.getGenres())
            fMap.put(genre, fMap.getOrDefault(genre, 0) + 1);
        Genre maxMin[] = new Genre[]{Genre.ACTION, Genre.ACTION};
        int max = 0, min = Integer.MAX_VALUE;
        for(Map.Entry<Genre, Integer> e : fMap.entrySet()) {
            if(e.getValue() > max) {
                maxMin[0] = e.getKey();
                max = e.getValue();
            }
            if(e.getValue() < min) {
                maxMin[1] = e.getKey();
                min = e.getValue();
            }
        }
        return database.getAllAnime().stream()
            .filter(x -> x.getGenre().contains(maxMin[0]) || x.getGenre().contains(maxMin[1]))
            .map(x -> new Output(x, railType))
            .toList();
    };

    public final Strategy LAST_WATCHED = () -> (animeList, railType) -> {
        Genre last = animeList.getGenres().get(animeList.getGenres().size() - 1);
        return database.getAllAnime().stream().filter(x -> x.getGenre().contains(last)).map(x -> new Output(x, railType))
        .toList();
    };
}
