package ladder.pager_duty.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ladder.pager_duty.enums.ThreatStatus;
import ladder.pager_duty.repo.IncidentRepo;
import ladder.pager_duty.store.Incident;

@Service
public class Reset {
    @Autowired private IncidentRepo repo;

    public void reset(String incidentID) {
        Incident incident = repo.findById(incidentID).orElse(null);
        if(incident != null) {
            incident.setThreatStatus(ThreatStatus.RESOLVED);
            repo.save(incident);
        }
    }
}
