package eosio.spectrum.websocket.api;

import eosio.spectrum.websocket.api.session.SessionIdAccounts;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


import java.util.UUID;

public class SessionIdAccountsTest {
    private SessionIdAccounts sessionIdAccounts;
    private String accountNameFirst_1;
    private String accountNameFirst_2;

    private String accountNameSecond;

    private String sessionIdFirst;
    private String sessionIdSecond;

    public UUID uiid;

    @Before
    public void init(){
        sessionIdAccounts = new SessionIdAccounts();
        accountNameFirst_1 = "accountNameFirst_1";
        accountNameFirst_2 = "accountNameFirst_2";
        accountNameSecond = "accountNameSecond";
        sessionIdFirst = UUID.randomUUID().toString();
        sessionIdSecond = UUID.randomUUID().toString();

        sessionIdAccounts.addAccount(sessionIdFirst,accountNameFirst_1);
        sessionIdAccounts.addAccount(sessionIdFirst,accountNameFirst_2);
        sessionIdAccounts.addAccount(sessionIdSecond,accountNameSecond);
    }

    @Test
    public void addAccount() {
        assertTrue(sessionIdAccounts.getAccounts(sessionIdFirst).contains(accountNameFirst_1));
        assertTrue(sessionIdAccounts.getAccounts(sessionIdFirst).contains(accountNameFirst_2));
        assertTrue(sessionIdAccounts.getAccounts(sessionIdSecond).contains(accountNameSecond));
    }

    @Test
    public void removSession(){
        sessionIdAccounts.removeSession(sessionIdFirst);
        assertTrue(sessionIdAccounts.containsSessionId(sessionIdSecond));
        assertFalse(sessionIdAccounts.containsSessionId(sessionIdFirst));
    }

    @Test
    public void removeAccount(){
        sessionIdAccounts.removeAccount(sessionIdFirst,accountNameFirst_1);
        assertTrue(sessionIdAccounts.getAccounts(sessionIdFirst).contains(accountNameFirst_2));
    }
}
