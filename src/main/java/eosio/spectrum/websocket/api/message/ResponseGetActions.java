package eosio.spectrum.websocket.api.message;

import eosio.spectrum.websocket.api.message.eosio.ActionTraces;

public class ResponseGetActions {

    RequestType requestType;
    ActionTraces action;

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public ActionTraces getAction() {
        return action;
    }

    public void setAction(ActionTraces action) {
        this.action = action;
    }
}
