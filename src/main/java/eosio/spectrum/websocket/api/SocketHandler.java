package eosio.spectrum.websocket.api;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import eosio.spectrum.websocket.api.redis.publishers.RedisMessagePublisherActions;
import eosio.spectrum.websocket.api.redis.publishers.RedisMessagePublisherBlocks;
import eosio.spectrum.websocket.api.redis.publishers.RedisMessagePublisherTableRows;
import eosio.spectrum.websocket.api.redis.publishers.RedisMessagePublisherTransaction;
import eosio.spectrum.websocket.api.message.FilteredAction;
import eosio.spectrum.websocket.api.message.FilteredBlock;
import eosio.spectrum.websocket.api.message.FilteredTransaction;
import eosio.spectrum.websocket.api.message.RequestType;
import eosio.spectrum.websocket.api.message.chronicle.BLOCK;
import eosio.spectrum.websocket.api.message.chronicle.TBL_ROW;
import eosio.spectrum.websocket.api.message.chronicle.TX_TRACE;
import eosio.spectrum.websocket.api.message.eosio.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;


@Component
@EnableWebSocket
public class SocketHandler extends BinaryWebSocketHandler implements WebSocketHandler {

    private static final transient Logger logger = LoggerFactory.getLogger(SocketHandler.class);

    private FilterRulesStorage filterRulesStorage;
    private RedisMessagePublisherActions redisMessagePublisherActions;
    private RedisMessagePublisherTransaction redisMessagePublisherTransaction;
    private RedisMessagePublisherBlocks redisMessagePublisherBlocks;
    private RedisMessagePublisherTableRows redisMessagePublisherTableRows;


@Autowired
public void setRedisMessagePublisherBlocks(RedisMessagePublisherBlocks redisMessagePublisherBlocks){
    this.redisMessagePublisherBlocks = redisMessagePublisherBlocks;
}
    @Autowired
    public void setRedisMessagePublisherTableRows(RedisMessagePublisherTableRows redisMessagePublisherTableRows){
        this.redisMessagePublisherTableRows = redisMessagePublisherTableRows;
    }
    @Autowired
    public void setRedisMessagePublisherTransaction(RedisMessagePublisherTransaction redisMessagePublisherTransaction){
        this.redisMessagePublisherTransaction = redisMessagePublisherTransaction;
    }
    @Autowired
    public void setFilterRulesStorage(FilterRulesStorage filterRulesStorage){
        this.filterRulesStorage =filterRulesStorage;
    }

    @Autowired
    public void setRedisMessagePublisherActions(RedisMessagePublisherActions redisMessagePublisherActions) {
        this.redisMessagePublisherActions = redisMessagePublisherActions;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("Session established from: " + session.getRemoteAddress());
    }

    @Override
    public void handleBinaryMessage(WebSocketSession session, BinaryMessage binaryMessage) throws UnsupportedEncodingException, JSONException {

        String stringMessage = new String(binaryMessage.getPayload().array(), "UTF-8");

        JSONObject jsonMessage = new JSONObject(stringMessage);
        String messageType = jsonMessage.get("msgtype").toString();
        switch (messageType) {
            case "ABI_UPDATED":
                logger.debug("Message type: " + messageType);
                break;
            case "FORK":
                logger.debug("Message type: " + messageType);
                break;
            case "TBL_ROW":
                try {
                    String code = jsonMessage.getJSONObject("data").getJSONObject("kvo").getString("code");
                    String scope = jsonMessage.getJSONObject("data").getJSONObject("kvo").getString("scope");
                    String table =  jsonMessage.getJSONObject("data").getJSONObject("kvo").getString("table");
                    GetTableRowsRule getTableRowsRule = new GetTableRowsRule();
                    getTableRowsRule.setCode(code);
                    getTableRowsRule.setScope(scope);
                    getTableRowsRule.setTable(table);
                    if (filterRulesStorage.getRules(RequestType.get_table_rows).containsKey(getTableRowsRule)){

                        redisMessagePublisherTableRows.publish(jsonMessage.getJSONObject("data").toString());
                    }
                }catch (JsonSyntaxException jse){
                    logger.warn(stringMessage);
                }
                break;
            case "BLOCK":
                try {
                    if(!filterRulesStorage.getRules(RequestType.get_blocks).isEmpty()){
                        Gson gson = new GsonBuilder().
                                registerTypeAdapter(Trx.class, new TrxDeserializer()).create();
                        BLOCK chronicleBlock = gson.fromJson(stringMessage, BLOCK.class);
                        Block block = chronicleBlock.getBlockData().getBlock();
                        block.setBlock_num(chronicleBlock.getBlockData().getBlock_num());
                        block.setLas_irreversible(chronicleBlock.getBlockData().getLast_irreversible());
                        FilteredBlock filteredBlock = new FilteredBlock();
                        filteredBlock.setBlock(block);
                        redisMessagePublisherBlocks.publish(new Gson().toJson(filteredBlock));
                    }
                }catch (Exception exception){
                    logger.info(stringMessage);
                    logger.warn(exception.toString());
                }

                logger.debug("Message type: " + messageType);
                break;
            case "TX_TRACE":
                logger.debug("Message type: " + messageType);
                Gson gson = new GsonBuilder().registerTypeAdapter(Act.class, new ActDeserializer()).create();
                TX_TRACE tx_trace = gson.fromJson(stringMessage, TX_TRACE.class);
                    Transaction transaction = tx_trace.getTransaction();
                    List<FilteredAction> filteredActions = transaction.getActionsFiltered(filterRulesStorage.getRules(RequestType.get_actions));
                    if ( filteredActions.size() > 0) {
                        for (FilteredAction filteredAction : filteredActions) {
                            redisMessagePublisherActions.publish(
                                    new Gson().toJson(filteredAction));
                        }
                    }
                    List<FilteredTransaction> filteredTransactions = transaction.getTransactionFiltered(filterRulesStorage.getRules(RequestType.get_transaction));
                    if (filteredTransactions.size() >0 ){
                        for (FilteredTransaction filteredTransaction : filteredTransactions) {
                            redisMessagePublisherTransaction.publish(
                                    new Gson().toJson(filteredTransaction));
                        }
                    }

                break;
            case "BLOCK_COMPLETED":
                try {
                    String blockNumber = jsonMessage.
                            getJSONObject("data").
                            getString("block_num");
                    logger.debug("acknowleged block number: : " + blockNumber);
                    if (Integer.valueOf(blockNumber) % 100==0){
                        logger.info("acknowleged block number: "+blockNumber);
                    }
                    session.sendMessage(new BinaryMessage(blockNumber.getBytes()));

                } catch (JSONException jex) {
                    logger.error("JSON Parse error", jex);
                } catch (IOException ioex) {
                    logger.error("IO Exception", ioex);
                }
                break;
            default:
                logger.debug("Unknown message type: " + messageType);
                break;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.info("Chronicle was disconnected "+status.getReason());
    }

}

