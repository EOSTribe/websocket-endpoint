package eosio.spectrum.websocket.api;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import eosio.spectrum.websocket.api.configuration.Properties;
import eosio.spectrum.websocket.api.message.chronicle.ChronicleMessage;
import eosio.spectrum.websocket.api.message.eosio.Transaction;
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
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;


@Component
@EnableWebSocket
public class SocketHandler extends BinaryWebSocketHandler implements WebSocketHandler{

    private static final transient Logger logger = LoggerFactory.getLogger(SocketHandler.class);

    private ElasticSearchPublisher elasticSearchPublisher;

    private Boolean pubActions = false;
    private Boolean pubTransferActions = false;
    private Boolean pubNewAccountActions = false;
    private Boolean pubTransaction = false;
    private List filteredActions;

    @Autowired
    public void setProperties(Properties properties){
        if (!properties.getActionsIndex().isEmpty()){
            this.pubActions = true;
        }
        if (!properties.getTransferIndex().isEmpty()){
            this.pubTransferActions = true;
        }
        if (!properties.getNewAccountIndex().isEmpty()){
            this.pubNewAccountActions = true;
        }
        if (!properties.getTransactionIndex().isEmpty()){
            this.pubTransaction = true;
        }
        filteredActions= Arrays.asList(properties.getFilteredActions());
    }

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
        switch (messageType) {
            case "ABI_UPDATED":
                logger.debug("Message type: " + messageType);
                break;
            case "FORK":
                logger.debug("Message type: " + messageType);
                break;
            case "BLOCK":
                logger.debug("Message type: " + messageType);
                break;
            case "TBL_ROW":
                logger.info("Message type: " + messageType);
                break;
            case "TX_TRACE":
                try {

                ChronicleMessage chronicleMessage = new Gson().fromJson(stringMessage, ChronicleMessage.class);
                Transaction transaction = chronicleMessage.getTransaction();


                transaction.getTrace().getPartial().
                        setContext_free_data(
                                jsonMessage.getJSONObject("data").
                                        getJSONObject("trace").
                                        getJSONObject("partial").
                                        get("context_free_data")
                                        .toString());

                if (pubActions){
                    elasticSearchPublisher.pubActions(transaction.getActions());
                    }
                if (pubTransferActions){elasticSearchPublisher.
                        pubTransferActions(transaction.getActionsFiltered(filteredActions));
                    }
                if (pubNewAccountActions){
                    elasticSearchPublisher.pubNewAccountActions(transaction.
                        getActionsFiltered("newaccount"));
                    }
                if (pubTransaction){
                    transaction.getTrace().setAction_traces(null);
                    elasticSearchPublisher.pubTransaction(transaction);
                    }


                if (elasticSearchPublisher.getFailureState()){
                    logger.error("Elasticsearch connection is broken");
                    session.close();
                }
            }catch (IOException exception){
                    logger.error(exception.getMessage());
            }catch (JSONException jsonexception){
            logger.warn("jsonMessage is: "+jsonMessage.toString());
            }catch (JsonSyntaxException exception){
                    logger.error(exception.getMessage());
                logger.error(stringMessage);
                }
                break;
            case "BLOCK_COMPLETED":
                logger.debug("Message type: "+ messageType);
                try {
                    String blockNumber = jsonMessage.
                            getJSONObject("data").
                            getString("block_num");
                    if (session.isOpen()){
                        logger.info("Acknowledged block number: "+blockNumber);
                        session.sendMessage(new BinaryMessage(blockNumber.getBytes()));
                    }

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
