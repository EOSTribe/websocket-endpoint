package algorand.spectrum.websocket.api.listeners;

import algorand.spectrum.websocket.api.RestClient;
import algorand.spectrum.websocket.api.Rule;
import algorand.spectrum.websocket.api.TxType;
import algorand.spectrum.websocket.api.publishers.BlocksPublisher;
import algorand.spectrum.websocket.api.publishers.TransactionsPublisher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Block {
    private static final transient Logger logger = LoggerFactory.getLogger(Block.class);
    private RestClient restClient;
    private RedisTemplate redisMyTemplate;
    private TransactionsPublisher transactionsPublisher;
    private BlocksPublisher blocksPublisher;
    @Autowired
    public void setBlocksPublisher(BlocksPublisher blocksPublisher){
        this.blocksPublisher = blocksPublisher;
    }
    @Autowired
    public void setTransactionsPublisher(TransactionsPublisher transactionsPublisher){
        this.transactionsPublisher = transactionsPublisher;
    }
    @Autowired
    public void setRestClient(RestClient restClient){
        this.restClient = restClient;
    }
    @Autowired
    public void setRedisMyTemplate(RedisTemplate redisMyTemplate){
        this.redisMyTemplate = redisMyTemplate;
    }

    public void handleMessage(String messageBlock) {
        String block = restClient.getBlock(messageBlock);
        JSONArray trxs = getTransactions(block);
        if (trxs != null) {
            processTransactions(trxs);
        }
    }
    public JSONArray getTransactions(String block){
        JSONObject jsonBlock = new JSONObject(block);
        JSONObject txns = jsonBlock.getJSONObject("txns");
        JSONArray trxs = null;
        if (txns.length() > 0 ) {
            trxs = txns.getJSONArray("transactions");
        }
        return trxs;
    }
    public void processTransactions(JSONArray trxs){
            for (Object trx:trxs) {
                TxType txType = getTxType((JSONObject) trx);
                List<String> txAccounts = getTxAccounts((JSONObject)trx,txType);
                for (String txAccount: txAccounts){
                    Rule rule =(Rule) redisMyTemplate.opsForValue().get(txAccount);
//                    Add the data as we will use it later when sending the transaction to the websocket
                    ((JSONObject) trx).append("rule",rule);
                    if (rule != null){
                        transactionsPublisher.publish(trx.toString());
                    }
                }
            }
            logger.info(trxs.toString());
        }


    private TxType getTxType(JSONObject json){
        switch (json.getString("type")){
            case ("pay"):
                return TxType.pay;
            case ("keyreg"):
                return TxType.keyreg;
            case ("acfg"):
                return TxType.acfg;
            case ("axfer"):
                return TxType.axfer;
            case ("afrz"):
                return TxType.afrz;
            default:
                return TxType.unknown;
        }
    }

    private List getTxAccounts(JSONObject trx, TxType txType){
        List<String> accounts;
        switch (txType){
            case pay:
                accounts = new ArrayList<>();
                accounts.add(trx.getString("from"));
                accounts.add(trx.getJSONObject("payment").getString("to"));
                return accounts;
            case acfg:
                logger.info(trx.toString());
                return new ArrayList();
            case afrz:
                logger.info(trx.toString());
                return new ArrayList();
            case axfer:
                accounts = new ArrayList<>();
                accounts.add(trx.getString("from"));
                accounts.add(trx.getJSONObject("curxfer").getString("rcv"));
                logger.info(trx.toString());
                return accounts;
            case keyreg:
                accounts = new ArrayList<>();
                accounts.add(trx.getString("from"));
                logger.info(trx.toString());
                return accounts;
            default:
                return new ArrayList();
        }
    }
}
