package distributed.saga.core;

import org.springframework.stereotype.Component;

import distributed.saga.data.CommandData;
import distributed.saga.data.CommandState;
import distributed.saga.data.Data;
import distributed.saga.model.Handler;

@Component("identityHandler")
public class IdentityHandler implements Handler {
    private Handler nextNode;
    private static final String SERVICE_CONNECTION = "identity";
    private CommandState prevCommandState;

    @Override public CommandData createCommand(Data data) {
        if(prevCommandState == null) {
            prevCommandState = CommandState.RECEIVE;
            return CommandData.builder().data(data).commandState(CommandState.RECEIVE).targetBusiness("identity").build();
        }
        if(null == data.getState()) {
            if(prevCommandState == CommandState.RECEIVE) {
                prevCommandState = CommandState.EXECUTE;
                return CommandData.builder().data(data).commandState(CommandState.EXECUTE).targetBusiness("identity").build();
            } else if(prevCommandState == CommandState.EXECUTE) {
                prevCommandState = CommandState.EXTRACT;
                return CommandData.builder().data(data).commandState(CommandState.EXTRACT).targetBusiness("identity").build();
            }
        } else { // In case of failure of data
            switch (data.getState()) {
                case RETRY -> {
                    return CommandData.builder().data(data).commandState(prevCommandState).targetBusiness("identity").build();
                }
                case ERROR -> {
                    prevCommandState = CommandState.COMPENSATE;
                    return CommandData.builder().data(data).commandState(CommandState.COMPENSATE).targetBusiness("identity").build();
                }
                default -> {
                    if(prevCommandState == CommandState.RECEIVE) {
                        prevCommandState = CommandState.EXECUTE;
                        return CommandData.builder().data(data).commandState(CommandState.EXECUTE).targetBusiness("identity").build();
                    } else if(prevCommandState == CommandState.EXECUTE || prevCommandState == CommandState.COMPENSATE) {
                        prevCommandState = CommandState.EXTRACT;
                        return CommandData.builder().data(data).commandState(CommandState.EXTRACT).targetBusiness("identity").build();
                    }
                }
            }
        }
        return null;
    }

    @Override public void next(Handler next) {this.nextNode = next;}
    @Override public Handler next() {return this.nextNode;}

    @Override public String getService() {return SERVICE_CONNECTION;}
}
