package ticketing.pipeline_reactive.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ticketing.pipeline_reactive.business.queues.TenantManager;
import ticketing.pipeline_reactive.data.Data;
import ticketing.pipeline_reactive.database.Database;
import ticketing.pipeline_reactive.inputs.Request;
import ticketing.pipeline_reactive.utils.Serializer;

@RestController
@RequestMapping("/pipeline-backpressure")
public class Controller {

    @Autowired private Serializer serializer;
    @Autowired private TenantManager manager;
    @Autowired private Database db;

    @PostMapping("/task")
    public ResponseEntity<Object> createTask(@RequestBody Request req) {
        Data data = new Data(req.getName(), req.getAmount(), req.getAccountType());
        data.setTransactionID(serializer.generateTransactionID());
        manager.attachTicket(data);
        return ResponseEntity.ok().body("Inserted with transaction ID "+data.getTransactionID());
    }

    @PostMapping("/task-list")
    public ResponseEntity<Object> createTask(@RequestBody List<Request> reqs) {
        List<String> ids = new ArrayList<>();
        for(Request req : reqs) {
            Data data = new Data(req.getName(), req.getAmount(), req.getAccountType());
            data.setTransactionID(serializer.generateTransactionID());
            manager.attachTicket(data);
            ids.add(data.getTransactionID());
        }
        return ResponseEntity.ok().body("Inserted with transaction IDs "+ids);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<Data> get(@PathVariable String id) {
        return ResponseEntity.ok().body(db.get(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok().body(db.getAll());
    }
}
