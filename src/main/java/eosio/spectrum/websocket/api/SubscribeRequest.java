package eosio.spectrum.websocket.api;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

public class SubscribeRequest {

    @NotNull
    private String account;
    private List<String> actions;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    @Override
    public String toString() {
        return "SubscribeRequest {" +
                "account='" + account + '\'' +
                ", actions=" + actions +
                '}';
    }
}

