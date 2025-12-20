package iam.aws.admin;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import iam.aws.data.output.Output;
import iam.aws.enums.Access;
import iam.aws.model.Handler;

@Service
public class ChainExecutor {
    @Autowired
    private ChainManager cm;

    public Object execute(Output inp) {
        List<Handler> lst = cm.chainHandlers(inp.getAccess());
        CompletableFuture<Output> f;
        try {
            for (Handler h : lst) {
                f = h.atomicExecution(inp);
                inp = f.get();
                if (inp.getAccess() == Access.DENIED && inp.getTknData().getTokenID() != null)
                    return inp.getTknData().getTokenID();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.getLocalizedMessage();
        }
        return inp.getAccess() == Access.DENIED ? "Chain failed" : "Chain passed";
    }
}
