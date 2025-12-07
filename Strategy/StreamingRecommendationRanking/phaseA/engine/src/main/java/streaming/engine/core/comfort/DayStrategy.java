package streaming.engine.core.comfort;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import streaming.engine.data.output.Output;
import streaming.engine.database.Database;
import streaming.engine.enums.data.AiringDay;
import streaming.engine.model.Strategy;

@Component
public class DayStrategy {

    @Autowired private Database database;

    public final Strategy MAX_DAY = () -> (animeList, railType) -> {
        Map<AiringDay, Integer> fMap = new EnumMap<>(AiringDay.class);
        for(AiringDay day : animeList.getActiveDays())
                fMap.put(day, fMap.getOrDefault(day, 0) + 1);
        AiringDay day[] = new AiringDay[]{null};
        int max = 0;
        for(Map.Entry<AiringDay, Integer> e : fMap.entrySet())
            if(e.getValue() > max) {
                day[0] = e.getKey(); max = e.getValue();
            }
        return database.getAllAnime().stream()
            .filter(x -> x.getDays().contains(day[0]))
            .map(x -> new Output(x, railType)).toList();
    };

    public final Strategy ALL_DAYS = () -> (animeList, railType) -> {
        Set<AiringDay> activeDays = animeList.getActiveDays().stream().collect(Collectors.toSet());
        return database.getAllAnime().stream()
            .filter(x -> {
                for(AiringDay day : x.getDays())
                    if(activeDays.contains(day))
                        return true;
                return false;
            })
            .map(x -> new Output(x, railType)).toList();
    };
}
