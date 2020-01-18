package eosio.spectrum.websocket.api.redis.listeners;

import com.google.gson.Gson;
import eosio.spectrum.websocket.api.session.SubscriberSessionStorage;
import eosio.spectrum.websocket.api.message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;


@Component
public class MessageListenerTransaction {

    private static final transient Logger logger = LoggerFactory.getLogger(MessageListenerTransaction.class);

    private SubscriberSessionStorage subscriberSessionStorage;

    @Autowired
    private void setSubscriberSessionStorage(SubscriberSessionStorage subscriberSessionStorage){
        this.subscriberSessionStorage = subscriberSessionStorage;
    }
    public void handleMessage(String message) {
        FilteredTransaction filteredTransaction = new Gson().fromJson(message, FilteredTransaction.class);
        ResponseGetTransaction responseGetTransaction = new ResponseGetTransaction();
        responseGetTransaction.setRequestType(RequestType.get_transaction);
        responseGetTransaction.setTransaction(filteredTransaction.getTrace());
        for (String sessionId : subscriberSessionStorage.getSessionsId(filteredTransaction.getAccountName(),RequestType.get_transaction)) {
            WebSocketSession session = subscriberSessionStorage.getSession(sessionId);
            synchronized (session) {
                if (session.isOpen()) try {
                    session.sendMessage(new TextMessage(new Gson().toJson(responseGetTransaction)));
                } catch (IOException exception){
                    logger.error(session.getRemoteAddress().toString());
                    logger.error(exception.toString());
                }
            }
        }
        logger.debug(message);

    }
}
