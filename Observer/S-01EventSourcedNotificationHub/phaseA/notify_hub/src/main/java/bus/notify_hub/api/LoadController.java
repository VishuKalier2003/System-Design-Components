package bus.notify_hub.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bus.notify_hub.core.Observer;
import bus.notify_hub.core.Publisher;
import bus.notify_hub.service.factory.Factory;

@RestController
@RequestMapping("/load")
public class LoadController {
    @Autowired private Factory factory;

    @PostMapping("/pub")
    public ResponseEntity<String> createPub() {
        return ResponseEntity.accepted().body(factory.createPublisher());
    }

    @PostMapping("/sub")
    public ResponseEntity<String> createSub() {
        return ResponseEntity.accepted().body(factory.createObserver());
    }

    @PutMapping("/connect/{pID}/{oID}")
    public ResponseEntity<String> bind(@PathVariable String pID, @PathVariable String oID) {
        Observer o = factory.getObserver(oID);
        Publisher p = factory.getPublisher(pID);
        p.subscribe(o);
        return ResponseEntity.accepted().body("Binding publisher ID "+pID+" with observer ID "+oID);
    }

    @PutMapping("/disconnect/{pID}/{oID}")
    public ResponseEntity<String> unbind(@PathVariable String pID, @PathVariable String oID) {
        Observer o = factory.getObserver(oID);
        Publisher p = factory.getPublisher(pID);
        p.unsubscribe(o);
        return ResponseEntity.accepted().body("Unbinding publisher ID "+pID+" with observer ID "+oID);
    }

    @GetMapping("/pub/registry")
    public ResponseEntity<Object> showPub() {
        return ResponseEntity.accepted().body(factory.getPubRegistry());
    }

    @GetMapping("/sub/registry")
    public ResponseEntity<Object> showSub() {
        return ResponseEntity.accepted().body(factory.getObsRegistry());
    }
}
