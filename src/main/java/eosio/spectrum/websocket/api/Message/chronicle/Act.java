package eosio.spectrum.websocket.api.Message.chronicle;

import java.util.List;

public class Act {
    private List<Authorization> authorization;

    public List<Authorization> getAuthorization() {
        return authorization;
    }

    public void setAuthorization(List<Authorization> authorization) {
        this.authorization = authorization;
    }
}
