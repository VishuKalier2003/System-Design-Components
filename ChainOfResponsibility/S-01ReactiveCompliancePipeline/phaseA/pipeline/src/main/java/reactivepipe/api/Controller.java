package reactivepipe.api;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactivepipe.data.Data;
import reactivepipe.database.Activity;
import reactivepipe.dto.Request;
import reactivepipe.dto.Response;
import reactivepipe.service.Orchestrator;
import reactivepipe.utils.Serializer;

@RestController
@RequestMapping("/pipeline")
public class Controller {

    @Autowired private Serializer serializer;
    @Autowired private Orchestrator orchestrator;
    @Autowired private Activity activity;

    @PostMapping("/transaction")
    public ResponseEntity<Object> createTransaction(@RequestBody Request req) {
        Data data = new Data();
        String transactionID = serializer.generateTransactionID();
        data.setTransactionID(transactionID);
        data.setAmount(req.getAmount());
        orchestrator.startPipeline(data);
        Response res = new Response(transactionID, "http://localhost:8000/pipeline/status/"+transactionID, "http://localhost:8000/pipeline/result/"+transactionID);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<Object> getStatus(@PathVariable String id) {
        var status = activity.get(id);
        if(status == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(Map.of("transactionID", id, "status", status.toString()));
    }

    @GetMapping("/result/{id}")
    public ResponseEntity<Object> getResult(@PathVariable String id) {
        CompletableFuture<Data> f = orchestrator.getFutureMap().get(id);
        if(f == null)
            return ResponseEntity.notFound().build();
        if(!f.isDone()) {
            return ResponseEntity.ok(Map.of(
                    "transactionID", id,
                    "status", "PROCESSING"
            ));
        }
        try {
            Data result = f.get();
            return ResponseEntity.ok(Map.of(
                    "transactionId", id,
                    "status", "COMPLETED",
                    "result", Map.of(
                        "amount", result.getAmount(),
                        "logs", result.getLogs()
                    )
            ));
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> allStates() {
        return ResponseEntity.ok(activity.viewAll());
    }
}
