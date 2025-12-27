package ladder.pager_duty.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder=true)
public class ChainVerdict {
    // The handlerPassed increases the count for each handler passed in a level, maintained by Engine itself
    private boolean handlerPassed;
    private String log;
}
