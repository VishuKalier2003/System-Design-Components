package beyondcorp.google.operations.database;

import org.springframework.beans.factory.annotation.Autowired;

import beyondcorp.google.model.Actions;
import beyondcorp.google.model.Operation;
import beyondcorp.google.service.func.UserProfile;
import beyondcorp.google.store.enums.DatabaseActions;
import beyondcorp.google.store.User;

public class UpdateOperation implements Operation {
    @Autowired private UserProfile userProfile;

    @Override public Actions operationConstant() {
        return DatabaseActions.UPDATE;
    }

    @Override public Object execute(Object input) {
        return userProfile.updateUser((User) input);
    }
}
