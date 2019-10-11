package eosio.spectrum.websocket.api.message.eosio;

public class Data {
    private String quantity;
    private String memo;
    private String from;
    private String to;
    private String receiver;
    private String bytes;
    private String payer;

//    private String owner;


    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
