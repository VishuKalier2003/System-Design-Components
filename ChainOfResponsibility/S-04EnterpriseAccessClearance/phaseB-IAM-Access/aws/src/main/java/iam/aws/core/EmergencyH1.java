package iam.aws.core;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import iam.aws.data.output.Output;
import iam.aws.enums.Access;
import iam.aws.errors.AccessDenied;
import iam.aws.model.Handler;
import iam.aws.repo.EmployeeRepo;

@Component
public class EmergencyH1 implements Handler {
    private final String dBname = "E1";
    private final Executor executor;
    private final EmployeeRepo eRepo;

    // technique: Constructor Injection - the annotated services are managed by Spring-boot lifecycle
    public EmergencyH1(EmployeeRepo eRepo, @Qualifier("threads") Map<String, Executor> mp) {
        this.eRepo = eRepo;
        this.executor = mp.get(dBname);
    }

    @Override
    public CompletableFuture<Output> atomicExecution(Output data) {
        return CompletableFuture.completedFuture(data).thenApplyAsync(fn -> {
            // fixed: If Access is Denied, no need to go to the next Handler, can simply fallback
            String name = data.getInfo().getRequester(), name1 = data.getInfo().getReceiver();
            if(!eRepo.existsById(name))
                    throw new AccessDenied("The employeeID "+name+"does not exists in the database");
            if(!eRepo.existsById(name1))
                    throw new AccessDenied("The employeeID "+name1+"does not exists in the database");
            return fn;
        }, executor).exceptionally(ex -> {
            data.setAccess(Access.DENIED);
            return data;
        });
    }
}
