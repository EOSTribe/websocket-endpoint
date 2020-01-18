package eosio.spectrum.websocket.api;

import eosio.spectrum.websocket.api.session.AccountSessionIds;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.UUID;

public class AccountSessionIdsTest {

    private AccountSessionIds accountSessionIds;

    private String accountNameFirst_1;
    private String accountNameFirst_2;

    private String accountNameSecond;

    private String sessionIdFirst;
    private String sessionIdFirst_2;
    private String sessionIdSecond;

    public UUID uiid;

    @Before
    public void init(){
        accountSessionIds = new AccountSessionIds();

        accountNameFirst_1 = "accountNameFirst_1";
        accountNameFirst_2 = "accountNameFirst_2";
        accountNameSecond = "accountNameSecond";
        sessionIdFirst = UUID.randomUUID().toString();
        sessionIdSecond = UUID.randomUUID().toString();
        accountSessionIds.addSession(accountNameFirst_1,sessionIdFirst);
        accountSessionIds.addSession(accountNameFirst_1,sessionIdSecond);
        accountSessionIds.addSession(accountNameSecond,sessionIdFirst);
        accountSessionIds.addSession(accountNameSecond,sessionIdSecond);
    }

    @Test
    public void getSessionIds(){
        assertTrue(accountSessionIds.getSessionIds(accountNameFirst_1).contains(sessionIdFirst));
        assertTrue(accountSessionIds.getSessionIds(accountNameFirst_1).contains(sessionIdSecond));
        assertTrue(accountSessionIds.getSessionIds(accountNameSecond).contains(sessionIdFirst));
        assertTrue(accountSessionIds.getSessionIds(accountNameSecond).contains(sessionIdSecond));
    }

    @Test
    public void removeSessionId(){
        accountSessionIds.removeSessionId(accountNameFirst_1,sessionIdFirst);
        assertTrue(accountSessionIds.getSessionIds(accountNameFirst_1).contains(sessionIdSecond));

        assertTrue(accountSessionIds.containsAccount(accountNameSecond));
    }


}
