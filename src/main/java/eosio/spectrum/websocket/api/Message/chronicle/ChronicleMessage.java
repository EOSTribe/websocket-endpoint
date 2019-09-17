package eosio.spectrum.websocket.api.Message.chronicle;

import com.google.gson.annotations.SerializedName;

public class ChronicleMessage {
    private ChronicleMessageType msgtype;
    @SerializedName("data")
    private Transaction transaction;

    public ChronicleMessageType getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(ChronicleMessageType msgtype) {
        this.msgtype = msgtype;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
