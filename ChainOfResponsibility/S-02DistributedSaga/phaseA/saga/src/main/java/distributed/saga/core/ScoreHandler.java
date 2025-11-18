package distributed.saga.core;

import org.springframework.stereotype.Component;

import distributed.saga.data.CommandData;
import distributed.saga.data.CommandState;
import distributed.saga.data.Data;
import distributed.saga.model.Handler;

@Component("scoreHandler")
public class ScoreHandler implements Handler {
    private Handler nextNode;
    private static final String SERVICE_CONNECTION = "score";
    private CommandState prevCommandState;

    @Override public CommandData createCommand(Data data) {
        if(prevCommandState == null) {
            prevCommandState = CommandState.RECEIVE;
            return CommandData.builder().data(data).commandState(CommandState.RECEIVE).targetBusiness("score").build();
        }
        if(null == data.getState()) {
            if(prevCommandState == CommandState.RECEIVE) {
                prevCommandState = CommandState.EXECUTE;
                return CommandData.builder().data(data).commandState(CommandState.EXECUTE).targetBusiness("score").build();
            } else if(prevCommandState == CommandState.EXECUTE) {
                prevCommandState = CommandState.EXTRACT;
                return CommandData.builder().data(data).commandState(CommandState.EXTRACT).targetBusiness("score").build();
            }
        } else { // In case of failure of data
            switch (data.getState()) {
                case RETRY -> {
                    return CommandData.builder().data(data).commandState(prevCommandState).targetBusiness("score").build();
                }
                case ERROR -> {
                    prevCommandState = CommandState.COMPENSATE;
                    return CommandData.builder().data(data).commandState(CommandState.COMPENSATE).targetBusiness("score").build();
                }
                default -> {
                    if(prevCommandState == CommandState.RECEIVE) {
                        prevCommandState = CommandState.EXECUTE;
                        return CommandData.builder().data(data).commandState(CommandState.EXECUTE).targetBusiness("score").build();
                    } else if(prevCommandState == CommandState.EXECUTE || prevCommandState == CommandState.COMPENSATE) {
                        prevCommandState = CommandState.EXTRACT;
                        return CommandData.builder().data(data).commandState(CommandState.EXTRACT).targetBusiness("score").build();
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
