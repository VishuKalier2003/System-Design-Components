package ladder.pager_duty.dto;

import java.time.Instant;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Evidence {
    private double weight;
    private String serviceName;
    private Instant createdAt;
}
