package eosio.spectrum.websocket.api.message;

import com.google.gson.annotations.SerializedName;

public enum RequestType {
    @SerializedName("get_actions")
    get_actions,
    @SerializedName("get_transaction")
    get_transaction,
    @SerializedName("get_table_rows")
    get_table_rows,
    @SerializedName("get_blocks")
    get_blocks,
    @SerializedName("ping")
    ping
}
