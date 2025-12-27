package ladder.pager_duty.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import ladder.pager_duty.repo.OperationLogRepo;
import ladder.pager_duty.store.OperationLog;

@Component
public class Logging {

    @Autowired
    private OperationLogRepo opLogRepo;

    @Transactional
    public void createOperationLog(List<String> logs, String incidentID) {
        OperationLog log = opLogRepo.findById(incidentID)
                .orElseGet(() -> {
                    OperationLog l = new OperationLog();
                    l.setIncidentID(incidentID);
                    return l;
                });
        log.getLogs().addAll(logs);
        // No save needed, Hibernate session manages automatically
    }

    public OperationLog get(String incidentID) {
        return opLogRepo.findById(incidentID).orElse(null);
    }

    @Transactional
    public List<String> getLogs(String incidentID) {
        return opLogRepo.findById(incidentID)
                .map(log -> List.copyOf(log.getLogs())) // forces initialization
                .orElse(List.of());
    }
}
