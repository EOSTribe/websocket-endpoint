package eosio.spectrum.websocket.api;

import eosio.spectrum.websocket.api.message.RequestType;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FilterRulesStorage {
    private HashMap<String,HashSet<String>> filtersGetActions = new HashMap<>();
    private HashMap<String,HashSet<String>> filtersGetTransaction = new HashMap<>();
    private HashMap<String,HashSet<String>> filtersGetBlocks = new HashMap<>();
    private HashMap<GetTableRowsRule,String> filtersGetTableRows = new HashMap<>();



    public void addRule(String account, HashSet<String> actions, RequestType requestType){
        switch (requestType){
            case get_table_rows:

                break;
            case get_transaction:
                filtersGetTransaction.put(account,null);
                break;
            case get_blocks:
                filtersGetBlocks.put(account,actions);
                break;
            case get_actions:
                if (filtersGetActions.containsKey(account)){
                    HashSet<String> actionsName = filtersGetActions.get(account);
                    for (String actionName:actions){
                        actionsName.add(actionName);
                    }
                    filtersGetActions.replace(account, actionsName);
                }else {
                    filtersGetActions.put(account,actions);
                }
                break;
        }

    }

    public void addRule(GetTableRowsRule getTableRowsRule, RequestType requestType){
        switch (requestType){
            case get_table_rows:
                this.filtersGetTableRows.put(getTableRowsRule,getTableRowsRule.getCode());
        }
    }

    public void removeRule(String account, HashSet<String> actions, RequestType requestType){
        switch (requestType){
            case get_table_rows:
                break;
            case get_transaction:
                filtersGetTransaction.remove(account);
                break;
            case get_blocks:
                break;
            case get_actions:
                if (filtersGetActions.containsKey(account)) {
                    HashSet<String> actionsName = filtersGetActions.get(account);
                    if (actionsName != null){
                        for (String actionName : actions) {
                            actionsName.remove(actionName);
                        }
                        filtersGetActions.replace(account, actionsName);
                    }else {
                        filtersGetActions.remove(account);
                    }
                }
                break;
        }

    }

    public void removeRule(String account, RequestType requestType){
        switch (requestType){
            case get_table_rows:
                for (Map.Entry<GetTableRowsRule,String> entry:filtersGetTableRows.entrySet()) {
                    if (entry.getValue().equals(account)){
                          filtersGetTableRows.remove(entry.getKey());
                    }
                }
                break;
            case get_transaction:
                filtersGetTransaction.remove(account);
                break;
            case get_blocks:
                filtersGetBlocks.remove(account);
                break;
            case get_actions:
                filtersGetActions.remove(account);
                break;
        }
    }

    public void removeRule(GetTableRowsRule getTableRowsRule, RequestType requestType){
        switch (requestType){
            case get_table_rows:
                filtersGetTableRows.remove(getTableRowsRule);
                break;
        }
    }

    public HashMap getRules(RequestType type){
        HashMap result;
        switch (type){
            case get_actions:
                result= filtersGetActions;
                break;
            case get_blocks:
                result = filtersGetBlocks;
                break;
            case get_transaction:
                result = filtersGetTransaction;
                break;
            case get_table_rows:
                result = filtersGetTableRows;
                break;
            default:
                result = null;
                break;
        }
        return result;
    }


}
