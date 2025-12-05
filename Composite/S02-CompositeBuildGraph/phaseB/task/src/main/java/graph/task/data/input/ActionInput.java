package graph.task.data.input;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder(toBuilder=true)
public class ActionInput {
    @Builder.Default
    private final AtomicInteger index = new AtomicInteger(0);
    private boolean retryFlag;
    private int retryCount;
    private List<String> actions;
    private final String reqID;

    public void addResourceRequest(String req) {this.actions.add(req);}

    public String currentResource() {return actions.get(index.intValue());}

    public void moveIndexForward() {index.incrementAndGet();}

    public void moveIndexBackward() {index.decrementAndGet();}

    public boolean hasResource() {return index.intValue() < actions.size();}
}
