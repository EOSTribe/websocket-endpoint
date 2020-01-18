package eosio.spectrum.websocket.api.message.eosio;

public class BlockData {
    private int block_num;
    private int last_irreversible;
    private Block block;

    public int getBlock_num() {
        return block_num;
    }

    public void setBlock_num(int block_num) {
        this.block_num = block_num;
    }

    public int getLast_irreversible() {
        return last_irreversible;
    }

    public void setLast_irreversible(int last_irreversible) {
        this.last_irreversible = last_irreversible;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
