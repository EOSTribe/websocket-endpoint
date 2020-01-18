package eosio.spectrum.websocket.api.NotUse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class TransactionProcessing {

    private JSONObject transactionMessage;
    private HashSet actionsSet;
    private HashMap get_actionsFilters;

    private static final transient Logger logger = LoggerFactory.getLogger(TransactionProcessing.class);


    public TransactionProcessing(JSONObject transactionMessage, HashMap get_actionsFilters) {
        this.transactionMessage = transactionMessage;
        this.get_actionsFilters =get_actionsFilters;
    }

    public ArrayList<JSONObject> getFilteredActions() {
        ArrayList<JSONObject> actions = new ArrayList<>();
        JSONObject jsonAction;
        JSONObject jsonTrace = this.transactionMessage.optJSONObject("trace");
        if(jsonTrace == null) {
            logger.error("Got null trace in transactionMessage " + this.transactionMessage.toString());
            return actions;
        }
        JSONArray jsonActionTraces = jsonTrace.optJSONArray("action_traces");
        if(jsonActionTraces == null) {
            logger.error("Got null action traces array from JSON Trace "+jsonTrace.toString());
            return actions;
        }
        for (Object action : jsonActionTraces) {
            if (action instanceof JSONObject) {
                jsonAction = (JSONObject) action;
                String actAuthorizationActor;
                String receiptReceiver;
                String actionName;
                try {
                    // Obtain actAuthorizationActor field
                    actAuthorizationActor = jsonAction.getJSONObject("act").
                            getJSONArray("authorization").
                            getJSONObject(0).
                            getString("actor");
                    // Obtain receiptReceiver field
                    receiptReceiver = jsonAction.getJSONObject("receipt").
                            getString("receiver");
                    actionName = jsonAction.getJSONObject("act").getString("name");

                    if (get_actionsFilters.containsKey(actAuthorizationActor)) {
                        // Obtain action names for given actAuthorizationActor
                        HashSet<String> actionsFiltered = (HashSet<String>) get_actionsFilters.get(actAuthorizationActor);

                        if ( (actionsFiltered == null) || actionsFiltered.contains(actionName) ){
                             jsonAction.put("block_num", this.transactionMessage.get("block_num"));
                             jsonAction.put("block_timestamp", this.transactionMessage.get("block_timestamp"));
                             jsonAction.put("trx", this.transactionMessage.getJSONObject("trace").get("id"));
                             actions.add(prepareMessage(actAuthorizationActor, jsonAction));
                             logger.info("actAuthorizationActor");
                        }
                    }
                    if (get_actionsFilters.containsKey(receiptReceiver)) {
                        // Obtain action names for given receiptReceive
                        HashSet<String> actionsFiltered = (HashSet<String>) get_actionsFilters.get(receiptReceiver);
                        if ( (actionsFiltered == null) || actionsFiltered.contains(actionName) ){
                            jsonAction.put("block_num", this.transactionMessage.get("block_num"));
                            jsonAction.put("block_timestamp", this.transactionMessage.get("block_timestamp"));
                            jsonAction.put("trx", this.transactionMessage.getJSONObject("trace").get("id"));
                            actions.add(prepareMessage(receiptReceiver, jsonAction));
                            logger.info("actAuthorizationActor");
                        }
                    }
                } catch (JSONException exception) {
                    logger.warn("Can't get JSON array: "+jsonAction.toString());
                }
            } else {
                logger.warn("Can't decode action: " + action.toString());
            }
        }
        return actions;
    }

    private JSONObject prepareMessage(String accountID, JSONObject action) {
        JSONObject message = new JSONObject();
        message.put("accountID", accountID);
        message.put("action", action);
        return message;
    }

    public JSONObject getTransaction() {
        String failedDtrxTrace;
        JSONObject transaction = this.transactionMessage;
        /**
         * Converting field failed_dtrx_trace to String
         */
        failedDtrxTrace = this.transactionMessage.
                getJSONObject("trace").
                get("failed_dtrx_trace").toString();
        transaction.getJSONObject("trace").put("failed_dtrx_trace", failedDtrxTrace);
        /**
         * removing filed actions from transaction
         */
        transaction.getJSONObject("trace").remove("actions");
        return transaction;
    }


}
