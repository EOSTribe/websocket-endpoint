package eosio.spectrum.websocket.api.message;

import eosio.spectrum.websocket.api.message.eosio.Block;

public class FilteredBlock {

    private Block block;

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
