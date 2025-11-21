package fabric.sharding.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fabric.sharding.requests.RouteRequest;
import fabric.sharding.service.ShardManager;

@RestController
@RequestMapping("/shard")
public class ShardController {
    @Autowired private ShardManager sm;

    @PostMapping("/route/lower")
    public ResponseEntity<String> updateRoute(@RequestBody RouteRequest req) {
        sm.shiftLower(req.getShard(), req.getUpdated());
        return ResponseEntity.accepted().body("Shard "+req.getShard()+" updated with new lower value as : "+req.getUpdated());
    }

    @PostMapping("/route/higher")
    public ResponseEntity<String> updateRouteII(@RequestBody RouteRequest req) {
        sm.shiftHigher(req.getShard(), req.getUpdated());
        return ResponseEntity.accepted().body("Shard "+req.getShard()+" updated with new higher value as : "+req.getUpdated());
    }

    @PutMapping("/reroute/{taskID}/{shardID}")
    public ResponseEntity<String> reroute(@PathVariable String taskID, @PathVariable String shardID) {
        String earlier = sm.getOriginalShardID(taskID);
        String later = sm.deferenceRoute(taskID, shardID);
        return ResponseEntity.accepted().body("Shard rerouting from "+earlier+" to "+later+" for taskID "+taskID);
    }

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.accepted().body(sm.showAllShards());
    }

    @GetMapping("/get/{shard}")
    public ResponseEntity<Object> get(@PathVariable String shard) {
        return ResponseEntity.accepted().body(sm.showShard(shard));
    }
}
