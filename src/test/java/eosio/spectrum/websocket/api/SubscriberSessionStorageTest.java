package eosio.spectrum.websocket.api;

import org.apache.kafka.common.protocol.types.Field;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;

public class SubscriberSessionStorageTest {
    @Test
    public void getAccounts() {

//        SubscriberSessionStorage subscriberSessionStorage = new SubscriberSessionStorage();
//        WebSocketSession webSocketSession = mock(WebSocketSession.class);
//        String websocketsessionID= webSocketSession.getId();
//        subscriberSessionStorage.saveSession(webSocketSession);

    }

    @Test
    public void saveSessionIdAccounts() {
        String account = "testaccount1";
        String account2 = "testaccount2";
//        ArrayList<String> accounts = new ArrayList<>();
        HashSet<String> accounts = new HashSet<>();
        accounts.add(account);
        accounts.add(account2);

        UUID uuid = UUID.randomUUID();
        String sessionID = uuid.toString();
        SubscriberSessionStorage subscriberSessionStorage = new SubscriberSessionStorage();

        subscriberSessionStorage.saveSessionIdAccounts(sessionID,account);
        subscriberSessionStorage.saveSessionIdAccounts(sessionID,account2);

        assertEquals(accounts,subscriberSessionStorage.getAccounts(sessionID));

        assertEquals(sessionID,subscriberSessionStorage.getSessionId(account));
    }

    @Test
    public void removeSessionIdAccounts() {
        String account = "testaccount1";
        String account2 = "testaccount2";
        HashSet<String> accounts = new HashSet<>();
        accounts.add(account);
        accounts.add(account2);

        UUID uuid = UUID.randomUUID();
        String sessionID = uuid.toString();
        SubscriberSessionStorage subscriberSessionStorage = new SubscriberSessionStorage();

        subscriberSessionStorage.saveSessionIdAccounts(sessionID,account);
        subscriberSessionStorage.saveSessionIdAccounts(sessionID,account2);

        subscriberSessionStorage.removeSessionIdAccounts(sessionID);


        assertNull(subscriberSessionStorage.getSessionId(account));

    }

    @Test
    public void getSessionId() {
    }
}