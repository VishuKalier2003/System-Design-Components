package streaming.engine.data;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder=true)
public class User {
    private String userName;
    private List<String> activeDays;
    // List of String so anime Indexes we need to feed here
    private List<String> watchedIndex;
}
