package eosio.spectrum.websocket.api.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource("classpath:global.properties")
@ConfigurationProperties
public class Properties {
    @Value("${ES_HOST1}")
    private String esHost1;
    @Value("${ES_HOST2}")
    private String esHost2;
     @Value("${ACTIONS_INDEX}")
    private String actionsIndex;
    @Value("${TRANSACTION_INDEX}")
    private String transactionIndex;
    @Value("${WEBSOCKET_PATH}")
    private String websocketPath;
    @Value("${NEW_ACCOUNT_INDEX}")
    private String newAccountIndex;
    @Value("${TRANSFER_INDEX}")
    private String transferIndex;
    @Value("${FILTERED_ACTIONS_NAMES}")
    private String[] filteredActions;

    public String[] getFilteredActions() {
        return filteredActions;
    }

    public void setFilteredActions(String[] filteredActions) {
        this.filteredActions = filteredActions;
    }

    public String getEsHost1() {
        return esHost1;
    }

    public String getEsHost2() {
        return esHost2;
    }

    public String getActionsIndex() {
        return actionsIndex;
    }

    public String getTransactionIndex() {
        return transactionIndex;
    }

    public String getWebsocketPath() {
        return websocketPath;
    }

    public String getNewAccountIndex() {
        return newAccountIndex;
    }

    public void setNewAccountIndex(String newAccountIndex) {
        this.newAccountIndex = newAccountIndex;
    }

    public String getTransferIndex() {
        return transferIndex;
    }

    public void setTransferIndex(String transferIndex) {
        this.transferIndex = transferIndex;
    }
}
