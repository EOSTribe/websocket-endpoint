package eosio.spectrum.websocket.api;

import eosio.spectrum.websocket.api.session.SubscriberSessionStorage;
import eosio.spectrum.websocket.api.message.RequestType;
import org.junit.Before;
import org.junit.Test;


import java.util.HashSet;
import java.util.UUID;

import static org.junit.Assert.*;

public class SubscriberSessionStorageTest {
    public SubscriberSessionStorage subscriberSessionStorage;
    public String userFirstSessionId;
    public String userFirstAccount1;
    public String userFirstAccount2;
    public UUID uiid;

    String userSecondSessionId;
    String userSecondAccount1;
    String userSecondAccount2;

    HashSet<String> accountsUserSecond;

    @Before
    public void init() throws Exception{
        subscriberSessionStorage = new SubscriberSessionStorage();

        userFirstAccount1 = "userFirstAccount1";
        userFirstAccount2 = "userFirstAccount2";

        uiid = UUID.randomUUID();
        userFirstSessionId = uiid.toString();
        uiid = UUID.randomUUID();
        userSecondSessionId = uiid.toString();

        userSecondAccount1 = "userSecondAccount1";
        userSecondAccount2 = "userSecondAccount2";


        subscriberSessionStorage.addAccount(userFirstSessionId,userFirstAccount1, RequestType.get_actions);
        subscriberSessionStorage.addAccount(userFirstSessionId,userFirstAccount2, RequestType.get_actions);

        subscriberSessionStorage.addAccount(userFirstSessionId,userFirstAccount1, RequestType.get_transaction);
        subscriberSessionStorage.addAccount(userFirstSessionId,userFirstAccount2, RequestType.get_transaction);

        subscriberSessionStorage.addAccount(userFirstSessionId,userFirstAccount1, RequestType.get_blocks);
        subscriberSessionStorage.addAccount(userFirstSessionId,userFirstAccount2, RequestType.get_blocks);

        subscriberSessionStorage.addAccount(userFirstSessionId,userFirstAccount1, RequestType.get_table_rows);
        subscriberSessionStorage.addAccount(userFirstSessionId,userFirstAccount2, RequestType.get_table_rows);

        subscriberSessionStorage.addAccount(userSecondSessionId,userSecondAccount1, RequestType.get_actions);
        subscriberSessionStorage.addAccount(userSecondSessionId,userSecondAccount2,RequestType.get_actions);

        subscriberSessionStorage.addAccount(userSecondSessionId,userSecondAccount1, RequestType.get_table_rows);
        subscriberSessionStorage.addAccount(userSecondSessionId,userSecondAccount2,RequestType.get_table_rows);

        subscriberSessionStorage.addAccount(userSecondSessionId,userSecondAccount1, RequestType.get_blocks);
        subscriberSessionStorage.addAccount(userSecondSessionId,userSecondAccount2,RequestType.get_blocks);




    }

    @Test
    public void getSessionsID() {

        assertTrue(subscriberSessionStorage.getSessionsId(userFirstAccount1, RequestType.get_actions).contains(userFirstSessionId));
        assertTrue(subscriberSessionStorage.getSessionsId(userFirstAccount2, RequestType.get_actions).contains(userFirstSessionId));

        assertTrue(subscriberSessionStorage.getSessionsId(userFirstAccount1, RequestType.get_transaction).contains(userFirstSessionId));
        assertTrue(subscriberSessionStorage.getSessionsId(userFirstAccount2, RequestType.get_transaction).contains(userFirstSessionId));

        assertTrue(subscriberSessionStorage.getSessionsId(userFirstAccount1, RequestType.get_table_rows).contains(userFirstSessionId));
        assertTrue(subscriberSessionStorage.getSessionsId(userFirstAccount2, RequestType.get_table_rows).contains(userFirstSessionId));

        assertTrue(subscriberSessionStorage.getSessionsId(userFirstAccount1, RequestType.get_blocks).contains(userFirstSessionId));
        assertTrue(subscriberSessionStorage.getSessionsId(userFirstAccount2, RequestType.get_blocks).contains(userFirstSessionId));


    }
    @Test
    public void getAccounts(){
        assertTrue(subscriberSessionStorage.getAccounts(userFirstSessionId, RequestType.get_actions).contains(userFirstAccount1));
        assertTrue(subscriberSessionStorage.getAccounts(userFirstSessionId, RequestType.get_actions).contains(userFirstAccount2));
        assertTrue(subscriberSessionStorage.getAccounts(userFirstSessionId, RequestType.get_transaction).contains(userFirstAccount1));
        assertTrue(subscriberSessionStorage.getAccounts(userFirstSessionId, RequestType.get_transaction).contains(userFirstAccount2));
        assertTrue(subscriberSessionStorage.getAccounts(userFirstSessionId, RequestType.get_blocks).contains(userFirstAccount1));
        assertTrue(subscriberSessionStorage.getAccounts(userFirstSessionId, RequestType.get_blocks).contains(userFirstAccount2));
        assertTrue(subscriberSessionStorage.getAccounts(userFirstSessionId, RequestType.get_table_rows).contains(userFirstAccount1));
        assertTrue(subscriberSessionStorage.getAccounts(userFirstSessionId, RequestType.get_table_rows).contains(userFirstAccount2));
    }


    @Test
    public void removeAccount() {
        subscriberSessionStorage.removeAccount(userFirstSessionId, userFirstAccount1,RequestType.get_actions);
        subscriberSessionStorage.removeAccount(userSecondSessionId, userSecondAccount1,RequestType.get_actions);
        assertTrue(subscriberSessionStorage.getSessionsId(userFirstAccount2,RequestType.get_actions).contains(userFirstSessionId));
        assertTrue(subscriberSessionStorage.getSessionsId(userSecondAccount2,RequestType.get_actions).contains(userSecondSessionId));

    }
//    @Test
//    public void getSession() {
//    }
}