package ladder.pager_duty.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ladder.pager_duty.api.input.EvidenceInput;
import ladder.pager_duty.api.input.LogId;
import ladder.pager_duty.service.TestService;
import ladder.pager_duty.utils.Logging;

@RestController
@RequestMapping("/pager")
public class Controller {
    @Autowired private TestService ts;
    @Autowired private Logging logging;

    @PostMapping("/create/incident")
    public ResponseEntity<Object> create() {
        try {
            return ResponseEntity.accepted().body(ts.createIncident());
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(e.getLocalizedMessage());
        }
    }

    @PostMapping("/create/evidence")
    public ResponseEntity<String> createEvidence(@RequestBody EvidenceInput eInput) {
        try {
            return ResponseEntity.accepted().body(ts.insertEvidence(eInput.getIncidentID(), eInput.getServiceName(), eInput.getSentence()));
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(e.getLocalizedMessage());
        }
    }

    @PutMapping("/set/min/{updated}")
    public ResponseEntity<String> updateMin(@PathVariable Double updated) {
        try {
            ts.setMin(updated);
            return ResponseEntity.accepted().body("Min Updated");
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(e.getLocalizedMessage());
        }
    }

    @PutMapping("/set/max/{updated}")
    public ResponseEntity<String> updateMax(@PathVariable Double updated) {
        try {
            ts.setMax(updated);
            return ResponseEntity.accepted().body("Min Updated");
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/logs")
    public ResponseEntity<Object> allLogs(@RequestBody LogId logID) {
        try {
            return ResponseEntity.accepted().body(logging.getLogs(logID.getId()));
        } catch(Exception e) {
            return ResponseEntity.internalServerError().body(e.getLocalizedMessage());
        }
    }
}
