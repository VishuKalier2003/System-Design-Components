package ladder.pager_duty.store;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import ladder.pager_duty.dto.Evidence;
import ladder.pager_duty.dto.Log;
import ladder.pager_duty.enums.ThreatLevel;
import ladder.pager_duty.enums.ThreatStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="incident")
public class Incident {
    @Id @Column(name="incidentID")
    private String incidentID;

    private Instant createdAt;

    private double confidence;

    @ElementCollection
    @CollectionTable(
        // name of the column in different table created
        name="incident_evidence",
        // creates a foreign key named 'incident_id' that maps to the primary key of Incident entity
        joinColumns=@JoinColumn(name="incident_id")
    )
    private List<Evidence> evidences;

    private ThreatLevel threatLevel;

    @ElementCollection
    @CollectionTable(
        name="incident_log",
        joinColumns=@JoinColumn(name="incident_id")
    )
    private List<Log> logs;

    private ThreatStatus threatStatus;

    public Incident() {
        this.incidentID = "not-assigned";
        this.createdAt = Instant.now();
        this.confidence = 0.0d;
        this.evidences = new ArrayList<>();
        this.logs = new ArrayList<>();
        this.threatLevel = ThreatLevel.THREAT_LEVEL_1;
        this.threatStatus = ThreatStatus.ACTIVE;
    }

    public void insertEvidence(Evidence evidence) {
        this.evidences.add(evidence);
    }

    public void insertLog(Log log) {
        this.logs.add(log);
    }
}
