package streaming.engine.admin;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import streaming.engine.data.input.Request;
import streaming.engine.enums.data.AiringDay;
import streaming.engine.model.Strategy;

@Component
public class ChainEngine {
    @Autowired @Qualifier("RegisteredStrategy") Map<String, Strategy> registry;

    public Strategy getActivityTechnique(Request req) {
        if(req.getActiveDays().size() >= 4)
            return registry.get("ActivityStrategy-high");
        else    return registry.get("ActivityStrategy-low");
    }

    public Strategy getGenreTechnique(Request req) {
        if(req.getActiveDays().size() >= 4)
            return registry.get("GenreStrategy-max");
        Set<AiringDay> set = req.getActiveDays().stream().collect(Collectors.toSet());
        if(set.contains(AiringDay.SATURDAY) && set.contains(AiringDay.SUNDAY))
            return registry.get("GenreStrategy-two");
        else
            return registry.get("GenreStrategy-last");
    }

    public Strategy getDayTechnique(Request req) {
        if(req.getGenres().size() >= 7)
            return registry.get("DayStrategy-max");
        else
            return registry.get("DayStrategy-all");
    }

    public Strategy getRandomTechnique(Request req) {
        if(req.getActiveDays().size() <= 4 && req.getGenres().size() <= 7)
            return registry.get("RandomStrategy-random");
        else
            return registry.get("RandomStrategy-neighbor");
    }
}
