package eosio.spectrum.websocket.api.message.eosio.TBL_ROW;

public class Data {
    private int block_num;
    private Boolean added;
    private Kvo kvo;
    private String block_timestamp;

    public int getBlock_num() {
        return block_num;
    }

    public void setBlock_num(int block_num) {
        this.block_num = block_num;
    }

    public Boolean getAdded() {
        return added;
    }

    public void setAdded(Boolean added) {
        this.added = added;
    }

    public Kvo getKvo() {
        return kvo;
    }

    public void setKvo(Kvo kvo) {
        this.kvo = kvo;
    }

    public String getBlock_timestamp() {
        return block_timestamp;
    }

    public void setBlock_timestamp(String block_timestamp) {
        this.block_timestamp = block_timestamp;
    }
}
