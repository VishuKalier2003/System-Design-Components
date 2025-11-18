package distributed.saga.api;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import distributed.saga.data.Data;
import distributed.saga.database.DataStore;
import distributed.saga.database.Database;
import distributed.saga.input.Request;
import distributed.saga.input.Response;
import distributed.saga.service.Orchestrator;
import distributed.saga.utils.Serializer;

@RestController
@RequestMapping("/saga")
public class Controller {
    @Autowired private Serializer serializer;
    @Autowired private Orchestrator orchestrator;
    @Autowired private Database database;
    @Autowired private DataStore dataStore;

    @PostMapping("/task")
    public ResponseEntity<Object> createTask(@RequestBody Request req) {
        String transactionID = serializer.generateTransactionID();
        Data data = Data.builder().transactionID(transactionID).user(req.getUser()).age(req.getAge()).aadhar(req.getAadhar()).logs(new ArrayList<>()).build();
        orchestrator.startOperation(data);
        return ResponseEntity.ok().body(new Response(transactionID, "http://localhost:8000/status/"+transactionID));
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<Object> status(@PathVariable String id) {
        return ResponseEntity.ok().body(database.show(id));
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<Object> get(@PathVariable String id) {
        return ResponseEntity.ok().body(dataStore.get(id));
    }
}
