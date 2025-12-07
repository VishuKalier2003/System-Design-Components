package streaming.engine.data.output;

import lombok.Getter;
import lombok.Setter;
import streaming.engine.data.Anime;
import streaming.engine.enums.data.RailType;

@Getter
@Setter
public class Output {
    private double score;
    private RailType railType;
    private Anime anime;

    public Output(Anime anime, RailType type) {
        this.anime = anime;
        this.railType = type;
        this.score = 0.0d;
    }
}
