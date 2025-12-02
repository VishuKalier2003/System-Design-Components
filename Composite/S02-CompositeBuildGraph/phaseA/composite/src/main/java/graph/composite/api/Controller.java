package graph.composite.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import graph.composite.api.data.Request;
import graph.composite.generator.CompositeFactory;
import graph.composite.model.Composite;
import graph.composite.service.ExecutionManager;
import graph.composite.service.NodeManager;
import graph.composite.utils.Configuration;

@RestController
@RequestMapping("/graph")
public class Controller {

    @Autowired private NodeManager nm;
    @Autowired private CompositeFactory cFactory;
    @Autowired private ExecutionManager em;

    @PostMapping("/node/root")
    public ResponseEntity<Object> createRoot() {
        try {
            return ResponseEntity.ok().body(nm.attachRoot(cFactory.createLeafNode(false)));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error when creating root : "+e.getLocalizedMessage());
        }
    }

    @PostMapping("/node/{flag}")
    public ResponseEntity<Object> createNode(@PathVariable Boolean flag) {
        try {
            Composite node = cFactory.createLeafNode(flag);
            return ResponseEntity.ok().body("Node created "+node.getName());
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body("Error when creating nodes : "+e.getLocalizedMessage());
        }
    }

    @GetMapping("/config/{head}")
    public ResponseEntity<Object> operate(@RequestBody List<Request> req, @PathVariable String head) {
        try {
            Map<String, Configuration> mp = new HashMap<>();
            for(Request r : req) {
                mp.put(r.getNode(), r.getData());
            }
            return ResponseEntity.accepted().body(em.executeGraph(head, mp));
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body("Error in execution : "+e.getLocalizedMessage());
        }
    }

    @GetMapping("/check/{head}")
    public ResponseEntity<Object> check(@PathVariable String head) {
        try {
            return ResponseEntity.accepted().body(em.isGraphValid(head));
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body("Error in dependency check : "+e.getLocalizedMessage());
        }
    }
}
