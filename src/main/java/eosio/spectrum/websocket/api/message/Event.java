package eosio.spectrum.websocket.api.message;

import com.google.gson.annotations.SerializedName;

public enum Event {
    @SerializedName("subscribe")
    subscribe,
    @SerializedName("unsubscribe")
    unsubscribe

}
