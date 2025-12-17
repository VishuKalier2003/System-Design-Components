package beyondcorp.google.operations.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import beyondcorp.google.model.Actions;
import beyondcorp.google.model.Operation;
import beyondcorp.google.service.func.AuditStore;
import beyondcorp.google.store.enums.AuditActions;

@Component
public class AuditAllOperation implements Operation {

    @Autowired private AuditStore audit;

    @Override public Actions operationConstant() {
        return AuditActions.ALL;
    }

    @Override public Object execute(Object input) {
        return audit.getAll();
    }
}
