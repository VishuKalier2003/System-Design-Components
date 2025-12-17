package beyondcorp.google.operations.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import beyondcorp.google.model.Actions;
import beyondcorp.google.model.Operation;
import beyondcorp.google.service.func.UserProfile;
import beyondcorp.google.store.enums.DatabaseActions;

@Component
public class GetOperation implements Operation {

    @Autowired private UserProfile userProfile;

    @Override public Actions operationConstant() {
        return DatabaseActions.GET;
    }

    @Override public Object execute(Object input) {
        return userProfile.getUserData((String)input);
    }
}
