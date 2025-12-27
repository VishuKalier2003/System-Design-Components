package ladder.pager_duty.api.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class EvidenceInput {
    private final String incidentID, serviceName, sentence;
}
