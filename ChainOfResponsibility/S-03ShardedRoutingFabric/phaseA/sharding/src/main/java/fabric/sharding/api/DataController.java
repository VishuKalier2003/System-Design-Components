package fabric.sharding.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fabric.sharding.data.Data;
import fabric.sharding.requests.TaskRequest;
import fabric.sharding.service.ShardManager;

@RestController
@RequestMapping("/shard-fabric")
public class DataController {
    @Autowired private ShardManager manager;

    @PostMapping("/task")
    public ResponseEntity<String> createTask(@RequestBody TaskRequest task) {
        Data data = Data.builder()
                .transactionID(task.getTransactionID())
                .customer(task.getCustomer())
                .creditRates(task.getCharges())
                .amount(task.getAmount())
                .pay(task.getPay()).
                logs(new LinkedHashMap<>()).build();
        boolean accepted = manager.insertIntoTenantQueue(data);
        return ResponseEntity.accepted().body("Task Acceptance : "+accepted);
    }

    @PostMapping("/multiple-task")
    public ResponseEntity<Object> createTasks(@RequestBody List<TaskRequest> tasks) {
        List<Boolean> accepts = new ArrayList<>();
        for(TaskRequest task : tasks) {
            Data data = Data.builder()
                .transactionID(task.getTransactionID())
                .customer(task.getCustomer())
                .creditRates(task.getCharges())
                .amount(task.getAmount())
                .pay(task.getPay()).
                logs(new LinkedHashMap<>()).build();
            accepts.add(manager.insertIntoTenantQueue(data));
        }
        return ResponseEntity.accepted().body("Tasks acceptance : "+accepts);
    }
}
