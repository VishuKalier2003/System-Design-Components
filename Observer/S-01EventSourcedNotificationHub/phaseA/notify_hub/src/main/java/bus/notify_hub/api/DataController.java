package bus.notify_hub.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bus.notify_hub.core.Publisher;
import bus.notify_hub.data.Data;
import bus.notify_hub.service.Graph;
import bus.notify_hub.service.factory.Factory;

@RestController
@RequestMapping("/data")
public class DataController {
    @Autowired private Factory factory;
    @Autowired private Graph graph;

    @PostMapping("/push/{amt}/{type}/{pID}")
    public ResponseEntity<String> pushEvent(@PathVariable int amt, @PathVariable String type, @PathVariable String pID) {
        Publisher p = factory.getPublisher(pID);
        Data data = new Data(amt, type);
        p.publish(data);
        return ResponseEntity.accepted().body("data instance published from publisher ID "+pID+" of amount "+amt);
    }

    @GetMapping("/subscribers/{pID}")
    public ResponseEntity<Object> showSubs(@PathVariable String pID) {
        return ResponseEntity.accepted().body(graph.getActiveObservers(pID));
    }
}
