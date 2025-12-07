package streaming.engine.data.input;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import streaming.engine.enums.data.AiringDay;
import streaming.engine.enums.data.Genre;

@Getter
@Setter
public class Request {
    private List<Genre> genres;
    private List<AiringDay> activeDays;

    public Request() {
        genres = new ArrayList<>();
        activeDays = new ArrayList<>();
    }

    public void addGenre(String genre) {
        genres.add(Genre.valueOf(genre.toUpperCase()));
    }

    public void addActiveDay(String day) {
        activeDays.add(AiringDay.valueOf(day.toUpperCase()));
    }
}
