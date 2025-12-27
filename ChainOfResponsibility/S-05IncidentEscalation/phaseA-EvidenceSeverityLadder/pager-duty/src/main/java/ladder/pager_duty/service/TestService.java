package ladder.pager_duty.service;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ladder.pager_duty.dto.Evidence;
import ladder.pager_duty.dto.Log;
import ladder.pager_duty.repo.IncidentRepo;
import ladder.pager_duty.store.Incident;
import ladder.pager_duty.utils.IDGenerator;

@Service
public class TestService {
    private double min = -1.0d, max = 1.0d;

    @Autowired private IDGenerator generator;
    @Autowired private IncidentRepo repo;

    // LogID needs to be generated via util folder
    public Incident createIncident() {
        Incident incident = new Incident();
        incident.setIncidentID(generator.generateID());
        repo.save(incident);
        return incident;
    }

    // returns the incidentID of either created or passed (whichever happens)
    @Transactional
    public String insertEvidence(String incidentID, String serviceName, String logSentence) {
        Evidence evidence = new Evidence(ThreadLocalRandom.current().nextDouble(min, max), serviceName, Instant.now());
        Log log = new Log(logSentence, generator.generateID());
        Incident incident = repo.findById(incidentID).orElse(createIncident());
        incident.insertEvidence(evidence);
        incident.insertLog(log);
        repo.save(incident);
        return incident.getIncidentID();
    }

    public Incident get(String incident) {
        return repo.findById(incident).orElse(null);
    }

    public void setMin(double value) {
        min = Math.max(-1.0d, value);
    }

    public void setMax(double value) {
        max = Math.min(value, 1.0d);
    }
}
