package beyondcorp.google.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import beyondcorp.google.error.InvalidationException;
import beyondcorp.google.error.NoOpException;
import beyondcorp.google.model.Actions;
import beyondcorp.google.model.Operation;
import beyondcorp.google.store.Token;

@Service
public class Dispatcher {
    private final CapabilityEnforce enforcer;
    private final Map<Actions, Operation> fnMap;

    // technique: Constructor Injection
    public Dispatcher(CapabilityEnforce enforce, List<Operation> operations) {
        enforcer = enforce;
        fnMap = operations.stream().collect(
            Collectors.toMap(
                Operation::operationConstant,
                e -> e
            )
        );
    }

    public Object dispatch(Token token, Object input) {
        if(!enforcer.validate(token)) {
            // fixed: provided invalidation error
            throw new InvalidationException(token.getFnName().toString());
        }
        Operation op = fnMap.get(token.getFnName());
        if(op == null) {
            // fixed: no operation exist valve
            throw new NoOpException(token.getFnName().toString());
        }
        return op.execute(input);
    }
}
