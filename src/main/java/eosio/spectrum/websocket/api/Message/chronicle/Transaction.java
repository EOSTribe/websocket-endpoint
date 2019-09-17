package eosio.spectrum.websocket.api.Message.chronicle;

import eosio.spectrum.websocket.api.Message.chronicle.Trace;

public class Transaction {
    private int block_num;
    private String block_timestamp;
    private Trace trace;

    public int getBlock_num() {
        return block_num;
    }

    public void setBlock_num(int block_num) {
        this.block_num = block_num;
    }

    public String getBlock_timestamp() {
        return block_timestamp;
    }

    public void setBlock_timestamp(String block_timestamp) {
        this.block_timestamp = block_timestamp;
    }

    public Trace getTrace() {
        return trace;
    }

    public void setTrace(Trace trace) {
        this.trace = trace;
    }
}
