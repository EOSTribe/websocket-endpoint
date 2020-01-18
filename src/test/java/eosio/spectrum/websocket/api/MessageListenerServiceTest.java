package eosio.spectrum.websocket.api;

import com.google.gson.Gson;
import eosio.spectrum.websocket.api.redis.listeners.MessageListenerService;
import eosio.spectrum.websocket.api.message.Data;
import eosio.spectrum.websocket.api.message.Event;
import eosio.spectrum.websocket.api.message.RequestType;
import eosio.spectrum.websocket.api.message.ServiceMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class MessageListenerServiceTest {

   @Autowired
   MessageListenerService messageListenerService;
    @Autowired
    FilterRulesStorage filterRulesStorage;

    ServiceMessage serviceMessage = new ServiceMessage();


    private String firstAccount;
    private String firstAccountAction_1;
    private String firstAccountAction_2;
    private HashSet<String> firstAccountActions;

    private String secondAccount;
    private String secondAccountActions_1;
    private String secondAccountActions_2;
    private HashSet<String> secondAccountActions;
    private HashMap<String, HashSet<String>> filterRuleFirstAccount;

    @Before
    public void setUp() throws Exception {
        messageListenerService.setFilterRulesStorage(filterRulesStorage);
        filterRuleFirstAccount = new HashMap<>();

//        serviceMessageFirst = new ServiceMessage();
//        serviceMessageSecond = new ServiceMessage();

        serviceMessage.setEvent(Event.subscribe);
        serviceMessage.setRequestType(RequestType.get_actions);
        Data firstAccountData = new Data();
        firstAccount = "firstAccount";
        firstAccountAction_1 = "firstAccountAction_1";
        firstAccountAction_2 = "firstAccountAction_2";
        firstAccountActions = new HashSet<>();
        firstAccountActions.add(firstAccountAction_1);
        firstAccountActions.add(firstAccountAction_2);
        firstAccountData.setAccount(firstAccount);
        firstAccountData.setActions(firstAccountActions);
        filterRuleFirstAccount.put(firstAccount, firstAccountActions);
        serviceMessage.setData(firstAccountData);
         }

    @Test
    public void handleMessage() {
        messageListenerService.handleMessage(new Gson().toJson(serviceMessage));
        assertEquals(filterRuleFirstAccount, filterRulesStorage.getRules(RequestType.get_actions));
    }
}