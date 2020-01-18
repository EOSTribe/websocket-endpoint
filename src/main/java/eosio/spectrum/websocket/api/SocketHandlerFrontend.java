package eosio.spectrum.websocket.api;

import com.google.gson.*;
import eosio.spectrum.websocket.api.redis.publishers.RedisMessagePublisherService;
import eosio.spectrum.websocket.api.session.SubscriberSessionStorage;
import eosio.spectrum.websocket.api.message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@EnableWebSocket
public class SocketHandlerFrontend extends TextWebSocketHandler implements WebSocketHandler {

    private static final transient Logger logger = LoggerFactory.getLogger(SocketHandlerFrontend.class);

    private SubscriberSessionStorage subscriberSessionStorage;

    private ServiceMessage serviceMessage;

    private RedisMessagePublisherService redisMessagePublisherService;

    private SubscriberRequest subscriberRequest;

    @Autowired
    public void setRedisMessagePublisherService(RedisMessagePublisherService redisMessagePublisherService) {
        this.redisMessagePublisherService = redisMessagePublisherService;
    }

    @Autowired
    public void setSubscriberSessionStorage(SubscriberSessionStorage subscriberSessionStorage) {
        this.subscriberSessionStorage = subscriberSessionStorage;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info(session.getUri().toString());
        subscriberSessionStorage.saveSession(session);
        logger.debug("Chronicle connected from: "+session.getRemoteAddress());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Data data;
        try {
            subscriberRequest = new Gson().fromJson(message.getPayload(), SubscriberRequest.class);

            if (subscriberRequest.getRequestType() == null | subscriberRequest.getEvent() == null)
            {
                String infoMessage = "Unable to proceed request, fill fields according to documentation";
                session.sendMessage(new TextMessage(infoMessage));
                logger.warn("Request from "+session.getRemoteAddress()+" has unknown format");
                logger.warn("message body : "+message.getPayload());
                session.close();

            }else
                {
                    switch (subscriberRequest.getEvent())
                    {
                        case subscribe:
                            switch (subscriberRequest.getRequestType())
                            {
                                case get_actions:
                                    try {
                                        serviceMessage = new ServiceMessage();
                                        subscriberSessionStorage.addAccount(session.getId(), subscriberRequest.getData().getAccount(),subscriberRequest.getRequestType());
                                        serviceMessage.setEvent(subscriberRequest.getEvent());
                                        serviceMessage.setRequestType(subscriberRequest.getRequestType());
                                        serviceMessage.setData(subscriberRequest.getData());
                                        redisMessagePublisherService.publish(new Gson().toJson(serviceMessage));
                                        logger.info("Received request");
                                        logger.info(new Gson().toJson(subscriberRequest));
                                    }catch (NullPointerException npe){
                                        if (session.isOpen()) {
                                            String infoMessage = "Unable to proceed request, fill fields according to documentation";
                                            session.sendMessage(new TextMessage(infoMessage));
                                            logger.warn("Request from "+session.getRemoteAddress()+" has unknown format");
                                            logger.warn("message body : "+message.getPayload());
                                            session.close();
                                        }
                                    }

                                    break;
                                case get_transaction:
                                    try {
                                        serviceMessage = new ServiceMessage();
                                        subscriberSessionStorage.addAccount(session.getId(), subscriberRequest.getData().getAccount(),subscriberRequest.getRequestType());
                                        serviceMessage.setEvent(subscriberRequest.getEvent());
                                        serviceMessage.setRequestType(subscriberRequest.getRequestType());
                                        serviceMessage.setData(subscriberRequest.getData());
                                        redisMessagePublisherService.publish(new Gson().toJson(serviceMessage));
                                        logger.info("Received request");
                                        logger.info(new Gson().toJson(subscriberRequest));
                                    }catch (NullPointerException npe){
                                        if (session.isOpen()) {
                                            String infoMessage = "Unable to proceed request, fill fields according to documentation";
                                            session.sendMessage(new TextMessage(infoMessage));
                                            logger.warn("Request from "+session.getRemoteAddress()+" has unknown format");
                                            logger.warn("message body : "+message.getPayload());
                                            session.close();
                                        }
                                    }
                                    break;
                                case get_table_rows:
                                    data = new Data();
                                    assert subscriberRequest.getData().getCode()!=null && subscriberRequest.getData().getScope()!=null;
                                    subscriberSessionStorage.addAccount(session.getId(), subscriberRequest.getData().getCode(), subscriberRequest.getRequestType());
                                    serviceMessage = new ServiceMessage();
                                    serviceMessage.setEvent(subscriberRequest.getEvent());
                                    serviceMessage.setRequestType(subscriberRequest.getRequestType());
                                    serviceMessage.setData(subscriberRequest.getData());
                                    redisMessagePublisherService.publish(new Gson().toJson(serviceMessage));

                                    break;
                                case get_blocks:
                                    serviceMessage = new ServiceMessage();
                                    subscriberSessionStorage.addAccount(session.getId(), session.getId()+"random", subscriberRequest.getRequestType());
                                    serviceMessage.setRequestType(RequestType.get_blocks);
                                    serviceMessage.setEvent(Event.subscribe);
                                    data = new Data();
                                    data.setAccount(session.getId());
                                    serviceMessage.setData(data);
                                    redisMessagePublisherService.publish(new Gson().toJson(serviceMessage));
                                    logger.info("Received request");
                                    logger.info(new Gson().toJson(subscriberRequest));
                                    break;
                                case ping:
                                    break;
                            }
                            break;
                        case unsubscribe:
                            switch (subscriberRequest.getRequestType()){
                                case get_actions:
                                    serviceMessage = new ServiceMessage();
                                    serviceMessage.setEvent(Event.unsubscribe);
                                    serviceMessage.setRequestType(RequestType.get_actions);
                                    serviceMessage.setData(subscriberRequest.getData());
                                    redisMessagePublisherService.publish(new Gson().toJson(serviceMessage));
                                    subscriberSessionStorage.removeAccount(session.getId(), subscriberRequest.getData().getAccount(), subscriberRequest.getRequestType());
                                    break;
                                case get_blocks:
                                    subscriberSessionStorage.removeSession(session.getId());
                                    break;
                                case get_transaction:

                                    break;
                                case get_table_rows:
                                    break;
                            }
                    break;
                default:
                    String infoMessage = "Value of event field is unknown";
                    session.sendMessage(new TextMessage(infoMessage));
                    logger.warn("Request from "+session.getRemoteAddress()+" has unknown format");
                    session.close();
                  }
            }
        }
        catch (JsonIOException|JsonSyntaxException e){
            String infoMessage = "Unknown type of request";
            session.sendMessage(new TextMessage(infoMessage));
            logger.warn("Request from "+session.getRemoteAddress()+" has unknown format");
            logger.warn(e.toString());
            session.close();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionID = session.getId();
        if (subscriberSessionStorage.getAccounts(sessionID)!=null){
            for (String account:subscriberSessionStorage.getAccounts(sessionID)){
                serviceMessage = new ServiceMessage();
                serviceMessage.setEvent(Event.unsubscribe);
                serviceMessage.setRequestType(RequestType.get_actions);
                Data data = new Data();
                data.setAccount(account);
                serviceMessage.setData(data);
                redisMessagePublisherService.publish(new Gson().toJson(serviceMessage));

                serviceMessage = new ServiceMessage();
                serviceMessage.setEvent(Event.unsubscribe);
                serviceMessage.setRequestType(RequestType.get_transaction);
                data = new Data();
                data.setAccount(account);
                serviceMessage.setData(data);
                redisMessagePublisherService.publish(new Gson().toJson(serviceMessage));
            }


            serviceMessage = new ServiceMessage();
            serviceMessage.setEvent(Event.unsubscribe);
            serviceMessage.setRequestType(RequestType.get_blocks);
            Data data = new Data();
            data.setAccount(sessionID);
            serviceMessage.setData(data);
            redisMessagePublisherService.publish(new Gson().toJson(serviceMessage));

            serviceMessage = new ServiceMessage();
            serviceMessage.setEvent(Event.unsubscribe);
            serviceMessage.setRequestType(RequestType.get_table_rows);
            data = new Data();
            for(String account:subscriberSessionStorage.getAccounts(sessionID)){
                data.setCode(account);
                serviceMessage.setData(data);
                redisMessagePublisherService.publish(new Gson().toJson(serviceMessage));
            }
        }
        subscriberSessionStorage.removeSession(sessionID);
    }
}
