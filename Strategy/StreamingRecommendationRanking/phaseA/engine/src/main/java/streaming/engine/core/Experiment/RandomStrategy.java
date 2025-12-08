package streaming.engine.core.Experiment;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import streaming.engine.data.output.Output;
import streaming.engine.database.Database;
import streaming.engine.enums.data.Genre;
import streaming.engine.model.Strategy;
import streaming.engine.service.GenreGraph;

@Component
public class RandomStrategy {
    @Autowired private GenreGraph genreGraph;
    @Autowired private Database db;

    public final Strategy RANDOM = () -> (animeList, railType) -> {
        int rand = ThreadLocalRandom.current().nextInt(0, genreGraph.total());
        Genre genre = genreGraph.getFromIndex(rand);
        return db.getAllAnime().stream().filter(x -> x.getGenre().contains(genre)).map(x -> new Output(x, railType, x.getRating())).toList();
    };

    public final Strategy RANDOM_NEIGHBOR = () -> (animeList, railType) -> {
        int rand = ThreadLocalRandom.current().nextInt(0, genreGraph.total());
        Set<Genre> genre = genreGraph.neighbors(genreGraph.getFromIndex(rand));
        return db.getAllAnime().stream().filter(x -> {
            for(Genre g : x.getGenre())
                if(genre.contains(g))
                    return true;
            return false;
        }).map(x -> new Output(x, railType, x.getRating())).toList();
    };
}
