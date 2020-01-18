package eosio.spectrum.websocket.api.message.eosio;

public class Auth_sequence {
    private int sequence;
    private String account;

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
