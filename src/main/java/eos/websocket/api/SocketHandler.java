package eos.websocket.api;


import org.elasticsearch.common.inject.Singleton;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;


@Singleton
@Component
@EnableWebSocket
public class SocketHandler extends BinaryWebSocketHandler implements WebSocketHandler,Observed{
    private static final transient Logger logger = LoggerFactory.getLogger(SocketHandler.class);

    private List<Observer> subscribers = new ArrayList<>();
    private ArrayList<JSONObject> actionsList;
    private RedisMessagePublisher redisMessagePublisher;


    public SocketHandler() {
        addObserver(new ActionsPublisher());

    }

    @Autowired
    public void setRedisMessagePublisher(RedisMessagePublisher redisMessagePublisher) {
        this.redisMessagePublisher = redisMessagePublisher;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("Session established from: "+session.getRemoteAddress());
    }

    @Override
    public void handleBinaryMessage(WebSocketSession session, BinaryMessage binaryMessage) throws UnsupportedEncodingException, JSONException {
        TransactionProcessing transactionProcessing;
        String stringMessage = new String(binaryMessage.getPayload().array(),"UTF-8");
        JSONObject jsonMessage = new JSONObject(stringMessage);
        String messageType = jsonMessage.get("msgtype").toString();
        switch (messageType){
            case "ABI_UPDATED":
                logger.debug("Message type: "+ messageType);
                break;
            case "FORK":
                logger.debug("Message type: "+ messageType);
                break;
            case "TABLE_DELTAS":
                logger.debug("Message type: "+ messageType);
                break;
            case "BLOCK":
                logger.debug("Message type: "+ messageType);
                break;
            case "TX_TRACE":
                logger.debug("Message type: "+ messageType);
                try {
                    transactionProcessing = new TransactionProcessing(jsonMessage.getJSONObject("data"));

                    actionsList = transactionProcessing.getFiltredActions();
                    if(actionsList.size() > 0) {
                        redisMessagePublisher.publish(actionsList.toString());
                    }
                    String blockNumber = jsonMessage.
                            getJSONObject("data").
                            getString("block_num");

                    if (Integer.valueOf(blockNumber) % 10 == 0){
                        session.sendMessage(new BinaryMessage(blockNumber.getBytes()));
                        logger.info("acknowleged block number: "+ blockNumber); }
                } catch (JSONException e) {
                     e.printStackTrace();
                } catch (IOException e){
                    e.printStackTrace();
                }

                break;

            case "BLOCK_COMPLETED":
                logger.debug("Message type: "+ messageType);

                try {
                    String blockNumber = jsonMessage.
                            getJSONObject("data").
                            getString("block_num");

                    session.sendMessage(new BinaryMessage(blockNumber.getBytes()));

                }catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e){
                    e.printStackTrace();
                }
                break;
            default:
                logger.debug("Message type undefined: "+ messageType);
                break;
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println(status);
    }


    @Override
    public void addObserver(Observer observer) {
        this.subscribers.add(observer);

    }

    @Override
    public void removeObserver(Observer observer) {

    }


    @Override
    public void notifyObservers() {
        for(Observer observer:subscribers){
            observer.handleEvent(actionsList);
        }

    }
}
