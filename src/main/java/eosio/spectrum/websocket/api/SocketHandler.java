package eosio.spectrum.websocket.api;


import com.google.gson.Gson;
import eosio.spectrum.websocket.api.Message.chronicle.ChronicleMessage;
import eosio.spectrum.websocket.api.Message.chronicle.Transaction;
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

import org.json.JSONObject;


@Component
@EnableWebSocket
public class SocketHandler extends BinaryWebSocketHandler implements WebSocketHandler{

    private static final transient Logger logger = LoggerFactory.getLogger(SocketHandler.class);

    private ElasticSearchPublisher elasticSearchPublisher;

    @Autowired
    public void setElasticSearchPublisher(ElasticSearchPublisher elasticSearchPublisher) {
        this.elasticSearchPublisher = elasticSearchPublisher;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("Session established from: "+session.getRemoteAddress());
    }

    @Override
    public void handleBinaryMessage(WebSocketSession session, BinaryMessage binaryMessage) throws UnsupportedEncodingException, JSONException {

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
            case "BLOCK":
                logger.debug("Message type: "+ messageType);
                break;
            case "TBL_ROW":
                logger.info("Message type: "+ messageType);
                break;
            case "TX_TRACE":
                ChronicleMessage chronicleMessage = new Gson().fromJson(stringMessage, ChronicleMessage.class);
                Transaction transaction = chronicleMessage.getTransaction();

                logger.debug("Message type: "+ chronicleMessage.getMsgtype());
                try {
                    TransactionProcessing transactionProcessing = new TransactionProcessing(jsonMessage.getJSONObject("data"));

                    elasticSearchPublisher.
                            pubActions(transactionProcessing.getActions());
                    elasticSearchPublisher.
                            pubTransaction(transactionProcessing.getTransaction());

                    if (transactionProcessing.getNewAccountActions() !=null){
                        elasticSearchPublisher.
                                pubNewAccountActions(transactionProcessing.
                                        getNewAccountActions());
                    }

                    if (transactionProcessing.getTransferActions() !=null){
                        elasticSearchPublisher.
                                pubTransferActions(transactionProcessing.
                                        getTransferActions());
                    }

                    Integer blockNumber = transaction.getBlock_num();

                    if (Integer.valueOf(blockNumber) % 100 == 0){
                        session.sendMessage(new BinaryMessage(blockNumber.toString().getBytes()));
                        logger.info("acknowleged block number: "+ blockNumber);
                    }

                    if (elasticSearchPublisher.getFailureState()){
                        logger.warn("Elasticsearch connection is broken");
                        session.close();
                    }

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

                } catch (JSONException jex) {
                    logger.error("JSON Parse error", jex);
                } catch (IOException ioex){
                    logger.error("IO Exception", ioex);
                }
                break;
            default:
                logger.debug("Message type undefined: "+ messageType);
                break;
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.info("Close staus: "+status.getReason());
    }

}
