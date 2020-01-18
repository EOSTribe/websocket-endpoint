package eosio.spectrum.websocket.api.message;

import eosio.spectrum.websocket.api.message.eosio.ActionTraces;
import eosio.spectrum.websocket.api.message.eosio.Trace;

public class ResponseGetTransaction {

    RequestType requestType;
    Trace transaction;

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public Trace getTransaction() {
        return transaction;
    }

    public void setTransaction(Trace transaction) {
        this.transaction = transaction;
    }
}
