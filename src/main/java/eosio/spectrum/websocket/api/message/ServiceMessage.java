package eosio.spectrum.websocket.api.message;

import com.google.gson.annotations.SerializedName;
import org.springframework.stereotype.Component;

@Component
public class ServiceMessage {



    @SerializedName("type")
    private RequestType type;

    @SerializedName("event")
    private Event event;

    @SerializedName("data")
    private Data data;
    public ServiceMessage() {
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public RequestType getRequestType() {
        return type;
    }

    public void setRequestType(RequestType type) {
        this.type = type;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {

        this.data = data;
    }


}
