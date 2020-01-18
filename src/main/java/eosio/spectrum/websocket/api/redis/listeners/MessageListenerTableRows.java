package eosio.spectrum.websocket.api.redis.listeners;

import com.google.gson.Gson;
import eosio.spectrum.websocket.api.message.FilteredAction;
import eosio.spectrum.websocket.api.message.RequestType;
import eosio.spectrum.websocket.api.message.ResponseGetActions;
import eosio.spectrum.websocket.api.session.SubscriberSessionStorage;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;


@Component
public class MessageListenerTableRows {

    private static final transient Logger logger = LoggerFactory.getLogger(MessageListenerTableRows.class);

    private SubscriberSessionStorage subscriberSessionStorage;

    @Autowired
    private void setSubscriberSessionStorage(SubscriberSessionStorage subscriberSessionStorage){
        this.subscriberSessionStorage = subscriberSessionStorage;
    }

    public void handleMessage(String message) {
        JSONObject jsonMessage = new JSONObject(message);

        for (String sessionId : subscriberSessionStorage.getSessionsId(jsonMessage.getJSONObject("kvo").getString("code"),RequestType.get_table_rows)) {
            WebSocketSession session = subscriberSessionStorage.getSession(sessionId);
            synchronized (session) {
                if (session.isOpen()) try {
                    session.sendMessage(new TextMessage(jsonMessage.put("response",RequestType.get_table_rows.toString()).toString()));
                } catch (IOException exception){
                    logger.error(session.getRemoteAddress().toString());
                    logger.error(exception.toString());
                }
            }
            logger.debug(message);
        }
    }
}
