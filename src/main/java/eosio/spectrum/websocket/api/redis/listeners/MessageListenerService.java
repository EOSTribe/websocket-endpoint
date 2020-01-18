package eosio.spectrum.websocket.api.redis.listeners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eosio.spectrum.websocket.api.FilterRulesStorage;
import eosio.spectrum.websocket.api.GetTableRowsRule;
import eosio.spectrum.websocket.api.message.RequestType;
import eosio.spectrum.websocket.api.message.ServiceMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MessageListenerService {

    private static final transient Logger logger = LoggerFactory.getLogger(MessageListenerService.class);

    private FilterRulesStorage filterRulesStorage;

    @Autowired
    public void setFilterRulesStorage(FilterRulesStorage filterRulesStorage){
        this.filterRulesStorage = filterRulesStorage;
    }

    public void handleMessage(String message) {
        logger.debug(message);
                Gson gson = new GsonBuilder().serializeNulls().create();
        ServiceMessage serviceMessage = gson.fromJson(message, ServiceMessage.class);

        switch (serviceMessage.getEvent()) {
            case subscribe:
                switch (serviceMessage.getRequestType()){
                    case get_actions:
                        filterRulesStorage.addRule(serviceMessage.getData().getAccount(),serviceMessage.getData().getActions(),RequestType.get_actions);
                        break;
                    case get_transaction:
                        filterRulesStorage.addRule(serviceMessage.getData().getAccount(), serviceMessage.getData().getActions(),RequestType.get_transaction);
                        break;
                    case get_table_rows:
                        GetTableRowsRule getTableRowsRule = new GetTableRowsRule();
                        getTableRowsRule.setTable(serviceMessage.getData().getTable());
                        getTableRowsRule.setScope(serviceMessage.getData().getScope());
                        getTableRowsRule.setCode(serviceMessage.getData().getCode());
                        filterRulesStorage.addRule(getTableRowsRule,serviceMessage.getRequestType());
                        break;
                    case get_blocks:
                        filterRulesStorage.addRule(serviceMessage.getData().getAccount(),serviceMessage.getData().getActions(),RequestType.get_blocks);
                        break;
                }
                break;
            case unsubscribe:
                switch (serviceMessage.getRequestType()){
                    case get_actions:
                        filterRulesStorage.removeRule(serviceMessage.getData().getAccount(), serviceMessage.getData().getActions(), RequestType.get_actions);
                        break;
                    case get_transaction:
                        filterRulesStorage.removeRule(serviceMessage.getData().getAccount(), serviceMessage.getData().getActions(), RequestType.get_transaction);
                        break;
                    case get_blocks:
                        filterRulesStorage.removeRule(serviceMessage.getData().getAccount(), RequestType.get_blocks);
                        break;
                    case get_table_rows:

                        filterRulesStorage.removeRule(serviceMessage.getData().getCode(),serviceMessage.getRequestType());
                }
                break;
        }
    }
}
