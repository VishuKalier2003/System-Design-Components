package reactivepipe.database;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Getter;
import reactivepipe.data.QueueStatus;

@Getter
@Component
public class Activity {
    private final Map<String, QueueStatus> status = new HashMap<>();

    public QueueStatus get(String id) {return status.get(id);}

    public void insertOrUpdate(String id, QueueStatus status) {this.status.put(id, status);}

    public Map<String, QueueStatus> viewAll() {return this.status;}
}
