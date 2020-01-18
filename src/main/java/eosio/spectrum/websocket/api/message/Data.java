package eosio.spectrum.websocket.api.message;

import java.util.HashSet;

public class  Data {
    private String account;
    private HashSet<String> actions;
    private String table;
    private String code;
    private String scope;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public HashSet<String> getActions() {
        return actions;
    }

    public void setActions(HashSet actions) {
            this.actions = actions;
    }

    @Override
    public String toString() {
        return "Data {" +
                "account='" + account + '\'' +
                ", actions=" + actions +
                '}';
    }
}
