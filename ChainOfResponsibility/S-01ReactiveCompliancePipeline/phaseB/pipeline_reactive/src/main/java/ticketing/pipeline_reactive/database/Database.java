package ticketing.pipeline_reactive.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import ticketing.pipeline_reactive.data.Data;

@Component
public class Database {
    private final Map<String, Data> database = new HashMap<>();

    public void insert(String id, Data data) {database.put(id, data);}

    public Data get(String id) {return this.database.get(id);}

    public List<Data> getAll() {return this.database.values().stream().toList();}
}
