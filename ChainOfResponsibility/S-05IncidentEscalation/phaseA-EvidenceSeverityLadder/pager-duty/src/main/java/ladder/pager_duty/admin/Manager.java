package ladder.pager_duty.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import ladder.pager_duty.enums.ThreatLevel;
import ladder.pager_duty.model.Handler;

@Service
public class Manager {
    private final List<List<Handler>> heads = new ArrayList<>();
    private final List<Handler> handlers;

    public Manager(List<Handler> lst) {
        this.handlers = lst;
    }

    @PostConstruct
    public void init() {
        // Make the heads one-based indexing
        this.heads.add(null);
        // Building the Threat Level 1 chain in order
        this.heads.add(new ArrayList<>());
        this.heads.get(1).addAll(handlers.stream().filter(x -> x.handlerLevel() == ThreatLevel.THREAT_LEVEL_1).toList());
        // Building the Threat Level 2 chain in order
        this.heads.add(new ArrayList<>());
        this.heads.get(2).addAll(handlers.stream().filter(x -> x.handlerLevel() == ThreatLevel.THREAT_LEVEL_2).toList());
        // Building the Threat Level 3 chain in order
        this.heads.add(new ArrayList<>());
        this.heads.get(3).addAll(handlers.stream().filter(x -> x.handlerLevel() == ThreatLevel.THREAT_LEVEL_3).toList());
    }

    public Handler getHead(int level) {
        return this.heads.get(level).get(0);
    }

    public List<Handler> getChain(int level) {
        return this.heads.get(level);
    }
}
