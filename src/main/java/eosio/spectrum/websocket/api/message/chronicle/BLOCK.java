package eosio.spectrum.websocket.api.message.chronicle;

import com.google.gson.annotations.SerializedName;
import eosio.spectrum.websocket.api.message.eosio.BlockData;

public class BLOCK {
    private ChronicleMessageType msgtype;
    @SerializedName("data")
    private BlockData blockData;

    public ChronicleMessageType getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(ChronicleMessageType msgtype) {
        this.msgtype = msgtype;
    }

    public BlockData getBlockData() {
        return blockData;
    }

    public void setBlockData(BlockData blockData) {
        this.blockData = blockData;
    }
}
