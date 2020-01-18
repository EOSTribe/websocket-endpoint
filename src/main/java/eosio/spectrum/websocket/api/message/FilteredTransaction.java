package eosio.spectrum.websocket.api.message;

import eosio.spectrum.websocket.api.message.eosio.Trace;

public class FilteredTransaction {

    private String accountName;
    private Trace trace;

    public FilteredTransaction(String accountName, Trace trace) {
        this.accountName = accountName;
        this.trace = trace;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Trace getTrace() {
        return trace;
    }

    public void setTrace(Trace trace) {
        this.trace = trace;
    }
}
