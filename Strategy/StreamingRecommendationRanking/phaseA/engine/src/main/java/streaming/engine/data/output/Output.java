package streaming.engine.data.output;

import lombok.Getter;
import lombok.Setter;
import streaming.engine.data.Anime;
import streaming.engine.enums.data.RailType;

@Getter
@Setter
public class Output {
    private double score;
    private int rating;
    private RailType railType;
    private Anime anime;

    public Output(Anime anime, RailType type, int rating) {
        this.anime = anime;
        this.railType = type;
        this.score = 0.0d;
        this.rating = rating;
    }

    public void show() {
        System.out.println("Score : "+score);
        System.out.println("Rail Type : "+railType);
        anime.show();
    }
}
