package ladder.pager_duty.store;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="logging")
public class OperationLog {
    @Id
    @Column(name="incidentID")
    private String incidentID;

    @ElementCollection
    @CollectionTable(
        name="logs",
        joinColumns=@JoinColumn(name="incident_id")
    )
    private List<String> logs;

    public OperationLog() {
        this.incidentID = "not-assigned";
        this.logs = new ArrayList<>();
    }
}
