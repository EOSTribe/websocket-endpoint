package eosio.spectrum.websocket.api.redis.listeners;

import com.google.gson.Gson;
import eosio.spectrum.websocket.api.session.SubscriberSessionStorage;
import eosio.spectrum.websocket.api.message.RequestType;
import eosio.spectrum.websocket.api.message.ResponseGetBlocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


@Component
public class MessageListenerBlocks {

    private static final transient Logger logger = LoggerFactory.getLogger(MessageListenerBlocks.class);

    private SubscriberSessionStorage subscriberSessionStorage;

    @Autowired
    private void setSubscriberSessionStorage(SubscriberSessionStorage subscriberSessionStorage){
        this.subscriberSessionStorage = subscriberSessionStorage;
    }

    public void handleMessage(String message) {
        ResponseGetBlocks responseGetBlocks = new Gson().fromJson(message, ResponseGetBlocks.class);
        Set sessionsID=subscriberSessionStorage.getSessionsId(RequestType.get_blocks);
        for (Object  session:sessionsID) {
            WebSocketSession webSocketSession = subscriberSessionStorage.getSession((String)session);
            try {
                synchronized (session) {
                    if (webSocketSession.isOpen())
                    webSocketSession.sendMessage(new TextMessage(new Gson().toJson(responseGetBlocks)));
                }
            }catch (IOException exception){
                logger.error(exception.toString());
            }
        }
        logger.debug(message);
    }
}
