package eosio.spectrum.websocket.api.redis.listeners;

import com.google.gson.Gson;
import eosio.spectrum.websocket.api.session.SubscriberSessionStorage;
import eosio.spectrum.websocket.api.message.FilteredAction;
import eosio.spectrum.websocket.api.message.RequestType;
import eosio.spectrum.websocket.api.message.ResponseGetActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;


@Component
public class MessageListenerActions {

    private static final transient Logger logger = LoggerFactory.getLogger(MessageListenerActions.class);

    private SubscriberSessionStorage subscriberSessionStorage;

    @Autowired
    private void setSubscriberSessionStorage(SubscriberSessionStorage subscriberSessionStorage){
        this.subscriberSessionStorage = subscriberSessionStorage;
    }

    public void handleMessage(String message) {
        FilteredAction filteredAction = new Gson().fromJson(message, FilteredAction.class);
        ResponseGetActions responseGetActions = new ResponseGetActions();
        responseGetActions.setRequestType(RequestType.get_actions);
        responseGetActions.setAction(filteredAction.getAction());
        for (String sessionId : subscriberSessionStorage.getSessionsId(filteredAction.getAccountName(),RequestType.get_actions)) {
            WebSocketSession session = subscriberSessionStorage.getSession(sessionId);
            synchronized (session) {
                if (session.isOpen()) try {
                    session.sendMessage(new TextMessage(new Gson().toJson(responseGetActions)));
                } catch (IOException exception){
                    logger.error(session.getRemoteAddress().toString());
                    logger.error(exception.toString());
                }
            }
            logger.debug(message);
        }
    }
}
