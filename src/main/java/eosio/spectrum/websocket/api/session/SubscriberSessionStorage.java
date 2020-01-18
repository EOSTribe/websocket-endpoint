package eosio.spectrum.websocket.api.session;
import eosio.spectrum.websocket.api.message.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class SubscriberSessionStorage {

    private static final transient Logger logger = LoggerFactory.getLogger(SubscriberSessionStorage.class);


    private Map<String, WebSocketSession> sessions;

    private AccountSessionIds accountSessionIdsGetActions ;
    private SessionIdAccounts sessionIdAccountsGetActions ;
    private AccountSessionIds accountSessionIdsGetTransaction ;
    private SessionIdAccounts sessionIdAccountsGetTransaction;
    private AccountSessionIds accountSessionIdsGetBlocks ;
    private SessionIdAccounts sessionIdAccountsGetBlocks ;
    private AccountSessionIds accountSessionIdsGetTblDeltas;
    private SessionIdAccounts sessionIdAccountsGetTblDeltas;
    private List<SessionIdAccounts> sessionIdAccountsList;
    private List<AccountSessionIds> accountSessionIdsList;

    public SubscriberSessionStorage(){
        sessions = new ConcurrentHashMap<>();
        accountSessionIdsGetActions = new AccountSessionIds();
        sessionIdAccountsGetActions = new SessionIdAccounts();
        accountSessionIdsGetTransaction = new AccountSessionIds();
        sessionIdAccountsGetTransaction = new SessionIdAccounts();
        accountSessionIdsGetBlocks = new AccountSessionIds();
        sessionIdAccountsGetBlocks = new SessionIdAccounts();
        accountSessionIdsGetTblDeltas = new AccountSessionIds();
        sessionIdAccountsGetTblDeltas = new SessionIdAccounts();

        sessionIdAccountsList = new ArrayList<>();
        sessionIdAccountsList.add(sessionIdAccountsGetActions);
        sessionIdAccountsList.add(sessionIdAccountsGetBlocks);
        sessionIdAccountsList.add(sessionIdAccountsGetTblDeltas);
        sessionIdAccountsList.add(sessionIdAccountsGetTransaction);
        accountSessionIdsList = new ArrayList<>();
        accountSessionIdsList.add(accountSessionIdsGetActions);
        accountSessionIdsList.add(accountSessionIdsGetBlocks);
        accountSessionIdsList.add(accountSessionIdsGetTblDeltas);
        accountSessionIdsList.add(accountSessionIdsGetTransaction);
    }
    public void saveSession(WebSocketSession session) {
        this.sessions.put(session.getId(), session);
    }

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
        for (SessionIdAccounts sessionIdAccounts:sessionIdAccountsList) {
            try {
                for (String account : sessionIdAccounts.getAccounts(sessionId)) {
                    for (AccountSessionIds accountSessionIds:accountSessionIdsList) {
                        accountSessionIds.removeSessionId(account, sessionId);
                    }
            }}catch (NullPointerException npe){

            }
            sessionIdAccounts.removeSession(sessionId);
            sessions.remove(sessionId);
        }
    }

    public void addAccount(String sessionId, String account, RequestType requestType) {
        switch (requestType){
            case get_actions:
                this.sessionIdAccountsGetActions.addAccount(sessionId,account);
                this.accountSessionIdsGetActions.addSession(account,sessionId);
                break;
            case get_blocks:
                this.sessionIdAccountsGetBlocks.addAccount(sessionId,account);
                this.accountSessionIdsGetBlocks.addSession(account,sessionId);
                break;
            case get_transaction:
                this.sessionIdAccountsGetTransaction.addAccount(sessionId,account);
                this.accountSessionIdsGetTransaction.addSession(account,sessionId);
                break;
            case get_table_rows:
                this.sessionIdAccountsGetTblDeltas.addAccount(sessionId,account);
                this.accountSessionIdsGetTblDeltas.addSession(account,sessionId);
                break;
        }

    }

    public void removeAccount(String sessionId, String account, RequestType requestType) {
        switch (requestType){
            case get_table_rows:
                this.accountSessionIdsGetTblDeltas.removeAccount(account);
                this.sessionIdAccountsGetTblDeltas.removeAccount(sessionId,account);
                break;
            case get_transaction:
                this.accountSessionIdsGetTransaction.removeAccount(account);
                this.sessionIdAccountsGetTransaction.removeAccount(sessionId,account);
                break;
            case get_blocks:
                this.accountSessionIdsGetBlocks.removeAccount(account);
                this.sessionIdAccountsGetBlocks.removeAccount(sessionId,account);
                break;
            case get_actions:
                this.accountSessionIdsGetActions.removeAccount(account);
                this.sessionIdAccountsGetActions.removeAccount(sessionId,account);
                break;
        }
    }

    public HashSet<String> getAccounts(String sessionId){
        HashSet<String> result = new HashSet<>();

            for (SessionIdAccounts sessionIdAccounts:sessionIdAccountsList){
                try {
                for (String account:sessionIdAccounts.getAccounts(sessionId)){
                    if (account != null) result.add(account);
                }
            }catch (NullPointerException npe){
                logger.error(npe.toString());
            }
            }


        return result;
    }

    public HashSet<String> getAccounts(String sessionId, RequestType requestType){
        HashSet<String> result = new HashSet<>();
        switch (requestType){
            case get_actions:
                result =  this.sessionIdAccountsGetActions.getAccounts(sessionId);
                break;
            case get_blocks:
                result = this.sessionIdAccountsGetBlocks.getAccounts(sessionId);
                break;
            case get_transaction:
                result = this.sessionIdAccountsGetTransaction.getAccounts(sessionId);
                break;
            case get_table_rows:
                result = this.sessionIdAccountsGetTblDeltas.getAccounts(sessionId);
                break;
        }
        return result;
    }

    public HashSet<String> getSessionsId(String account, RequestType requestType) {
        HashSet<String> result = new HashSet<>();
        switch (requestType){
            case get_actions:
                result = this.accountSessionIdsGetActions.getSessionIds(account);
                break;
            case get_transaction:
                result = this.accountSessionIdsGetTransaction.getSessionIds(account);
                break;
            case get_table_rows:
                result = this.accountSessionIdsGetTblDeltas.getSessionIds(account);
                break;
            case get_blocks:
                result = this.accountSessionIdsGetBlocks.getSessionIds(account);
                break;
        }
        return result;
    }

    public Set getSessionsId(RequestType requestType){
        Set result = new HashSet<>();
        switch (requestType){
            case get_blocks:
                result = sessionIdAccountsGetBlocks.getSessions();
        }
                return result;
    }

    public WebSocketSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }

}
