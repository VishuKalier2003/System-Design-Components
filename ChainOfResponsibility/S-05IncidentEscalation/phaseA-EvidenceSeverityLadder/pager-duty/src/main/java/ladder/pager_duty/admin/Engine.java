package ladder.pager_duty.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import ladder.pager_duty.dto.ChainVerdict;
import ladder.pager_duty.enums.ThreatLevel;
import ladder.pager_duty.enums.ThreatStatus;
import ladder.pager_duty.model.Handler;
import ladder.pager_duty.repo.IncidentRepo;
import ladder.pager_duty.store.Incident;
import ladder.pager_duty.utils.Logging;

@Service
public class Engine {
    @Autowired private IncidentRepo repo;
    @Autowired private Manager manager;
    @Autowired private Logging logging;

    @Autowired @Qualifier("threat") Map<ThreatLevel, Integer> mp;
    @Autowired @Qualifier("nextThreatLevel") Map<Integer, AtomicInteger> nextLevelMap;

    private final Map<Integer, Integer> escalateScore = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(Engine.class);

    @PostConstruct
    public void init() {
        this.escalateScore.put(1, 2);
        this.escalateScore.put(2, 3);
        this.escalateScore.put(3, 2);
    }

    // detail: single-threaded, if operation takes more than fixedRate time the next call will be immediate and can be delayed
    @Scheduled(fixedRate=10, timeUnit=TimeUnit.SECONDS)
    @Transactional
    public void executeIncidents() {
        List<Incident> incidents = repo.findAll().stream().filter(x -> x.getThreatStatus() == ThreatStatus.ACTIVE).toList();
        logger.info("ExecuteIncidents() function CALLED");
        if(incidents == null || incidents.isEmpty())
            return;
        for(Incident incident : incidents) {
            int chainIndex = mp.get(incident.getThreatLevel());
            int phase;
            List<String> logs = new ArrayList<>();
            do {
                phase = executeIncidentChain(incident, manager.getChain(chainIndex), logs);
                // The current Threat Level is enough escalation for specified Incident
                if(escalateScore.get(chainIndex) != phase) {
                    break;
                }
                logs.add("Escalation is being checked...");
                chainIndex = nextLevelMap.get(chainIndex).intValue();
            } while(phase != -1);
            logging.createOperationLog(logs, incident.getIncidentID());
        }
    }

    public int executeIncidentChain(Incident incident, List<Handler> chain, List<String> log) {
        final AtomicInteger phase = new AtomicInteger(0);
        for(Handler handler : chain) {
            ChainVerdict verdict = handler.atomicExecution(incident);
            // Logs updated here itself
            log.add(verdict.getLog());
            if(!verdict.isHandlerPassed())
                return -1;
        }
        return phase.intValue();
    }
}
