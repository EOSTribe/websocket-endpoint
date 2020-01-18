package eosio.spectrum.websocket.api.message;

import eosio.spectrum.websocket.api.message.eosio.ActionTraces;

public class FilteredAction {

    private String accountName;
    private ActionTraces action;

    public FilteredAction(String accountName, ActionTraces action) {
        this.accountName = accountName;
        this.action = action;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public ActionTraces getAction() {
        return action;
    }

    public void setAction(ActionTraces action) {
        this.action = action;
    }
}
