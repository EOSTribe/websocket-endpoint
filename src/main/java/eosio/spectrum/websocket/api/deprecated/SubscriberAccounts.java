package eosio.spectrum.websocket.api.NotUse;

import java.util.HashSet;

public class SubscriberAccounts {
    private String sessionID;
    private HashSet<String> accounts;

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
