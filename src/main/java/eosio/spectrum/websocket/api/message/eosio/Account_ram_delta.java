package eosio.spectrum.websocket.api.message.eosio;

public class Account_ram_delta {
    private int delta;
    private String account;

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
