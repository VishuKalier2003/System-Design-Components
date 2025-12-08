package streaming.engine.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import streaming.engine.admin.ChainManager;
import streaming.engine.data.input.HandlerInfo;

@RestController
@RequestMapping("/execute")
public class ExecutionController {
    @Autowired private ChainManager manager;

    @PostMapping("/create/root")
    public ResponseEntity<String> createHandlerRoot(@RequestBody HandlerInfo info) {
        try {
            manager.attachRoot(info);
            return ResponseEntity.accepted().body("Root set");
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body("Exception caused as : "+e.getLocalizedMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createHandler(@RequestBody HandlerInfo info) {
        try {
            manager.attachNewHandler(info);
            return ResponseEntity.accepted().body("Root set");
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body("Exception caused as : "+e.getLocalizedMessage());
        }
    }

    @GetMapping("/execute/{userID}")
    public ResponseEntity<Object> createHandlerRoot(@PathVariable String userID) {
        try {
            return ResponseEntity.accepted().body(manager.executeChain(userID));
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body("Exception caused as : "+e.getLocalizedMessage());
        }
    }

    @DeleteMapping("/del/{index}")
    public ResponseEntity<Object> del(@PathVariable int index) {
        try {
            manager.deleteHandler(index);
            return ResponseEntity.accepted().body("Handler deleted at index "+index);
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body("Exception caused as : "+e.getLocalizedMessage());
        }
    }

    @PutMapping("/configure/{id}/{strategy}")
    public ResponseEntity<String> configure(@PathVariable int id, @PathVariable String strategy) {
        try {
            manager.setHandlerCustomStrategy(id, strategy);
            return ResponseEntity.accepted().body("Handler strategy updated for index : "+id);
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body("Exception caused as : "+e.getLocalizedMessage());
        }
    }

    @PutMapping("/swap/{i1}/{i2}")
    public ResponseEntity<Object> swap(@PathVariable int i1, @PathVariable int i2) {
        try {
            manager.swap(i1, i2);
            return ResponseEntity.accepted().body("Handlers swapped successfully");
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body("Exception caused as : "+e.getLocalizedMessage());
        }
    }
}
