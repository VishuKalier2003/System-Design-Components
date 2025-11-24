package bus.notify_hub.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bus.notify_hub.component.HubManager;

@RestController
@RequestMapping("/hub")
public class HubController {
    @Autowired private HubManager hubManager;

    @GetMapping("/all")
    public ResponseEntity<Object> showSyncHub() {
        return ResponseEntity.accepted().body(hubManager.getRegistry());
    }

    @GetMapping("/graph")
    public ResponseEntity<Object> showGraph() {
        return ResponseEntity.accepted().body(hubManager.getG());
    }

    @GetMapping("/{type}")
    public ResponseEntity<Object> showHub(@PathVariable String type) {
        return ResponseEntity.accepted().body(hubManager.get(type));
    }
}
