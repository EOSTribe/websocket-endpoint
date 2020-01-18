package eosio.spectrum.websocket.api.NotUse;

import eosio.spectrum.websocket.api.message.SubscriberRequest;
import org.springframework.web.socket.WebSocketSession;
import java.util.List;

public class Customer{

    private WebSocketSession webSocketSession;
    private String websocketSessionId;
    private List<SubscriberRequest> subscriberRequests;

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public void setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    public String getWebsocketSessionId() {
        return websocketSessionId;
    }

    public void setWebsocketSessionId(String websocketSessionId) {
        this.websocketSessionId = websocketSessionId;
    }

    public List<SubscriberRequest> getSubscriberRequests() {
        return subscriberRequests;
    }

    public void setSubscriberRequests(List<SubscriberRequest> subscriberRequests) {
        this.subscriberRequests = subscriberRequests;
    }
}
