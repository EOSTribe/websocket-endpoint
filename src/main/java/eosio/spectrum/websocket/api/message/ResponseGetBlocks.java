package eosio.spectrum.websocket.api.message;

import eosio.spectrum.websocket.api.message.eosio.ActionTraces;
import eosio.spectrum.websocket.api.message.eosio.Block;

public class ResponseGetBlocks {

    RequestType requestType;
    Block block;

    public ResponseGetBlocks(){
        this.requestType = RequestType.get_blocks;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
