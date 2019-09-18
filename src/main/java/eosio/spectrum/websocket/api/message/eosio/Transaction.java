package eosio.spectrum.websocket.api.message.eosio;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private int block_num;
    private String block_timestamp;
    private Trace trace;

    public int getBlock_num() {
        return block_num;
    }

    public void setBlock_num(int block_num) {
        this.block_num = block_num;
    }

    public String getBlock_timestamp() {
        return block_timestamp;
    }

    public void setBlock_timestamp(String block_timestamp) {
        this.block_timestamp = block_timestamp;
    }

    public Trace getTrace() {
        return trace;
    }

    public void setTrace(Trace trace) {
        this.trace = trace;
    }

    public List<ActionTraces> getActions(){
        List<ActionTraces> actionTraces = this.getTrace().getAction_traces();
        for (ActionTraces action:actionTraces) {
            action.setBlock_num(this.getBlock_num());
            action.setBlock_timestamp(this.getBlock_timestamp());
            action.setTrxId(this.getTrace().getId());
        }
        return actionTraces;
    }
    public List<ActionTraces> getActionsFiltered(String actionName){
        List<ActionTraces> actionTraces = new ArrayList<>();
        for (ActionTraces action:this.getTrace().getAction_traces()) {
            if (action.getAct().getName().equals(actionName)){
                action.setBlock_num(this.getBlock_num());
                action.setBlock_timestamp(this.getBlock_timestamp());
                action.setTrxId(this.getTrace().getId());
                actionTraces.add(action);
            }
        }

        return actionTraces;
    }
}
