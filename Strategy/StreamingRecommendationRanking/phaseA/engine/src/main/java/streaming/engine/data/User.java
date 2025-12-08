package streaming.engine.data;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder=true)
public class User {
    // All details should be string
    private String userName;
    private List<String> activeDays;
    // List of String so anime Indexes we need to feed here
    private List<String> watchedIndex;

    public void addDay(String day) {
        if(this.activeDays != null)
            this.activeDays.add(day);
    }

    public void addWatchAnimeIndex(String index) {
        if(this.watchedIndex != null)
            this.watchedIndex.add(index);
    }

    public void addDays(List<String> days) {
        if(this.activeDays != null)
            this.activeDays.addAll(days);
    }

    public void addWatchAnimeIndices(List<String> indices) {
        if(this.watchedIndex != null)
            this.watchedIndex.addAll(indices);
    }
}
