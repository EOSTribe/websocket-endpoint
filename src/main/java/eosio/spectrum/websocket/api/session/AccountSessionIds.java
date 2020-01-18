package eosio.spectrum.websocket.api.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class describe relations one to many because of several subscribers can listen the same account
 */
public class  AccountSessionIds {

    private static final transient Logger logger = LoggerFactory.getLogger(AccountSessionIds.class);

    private Map<String, HashSet<String>> records;

    public AccountSessionIds(){
        this.records = new ConcurrentHashMap<>();
    }

    public  void addSession(String account, String sessionId){
        if (this.records.containsKey(account)){
            HashSet<String> sessions = this.records.get(account);
            sessions.add(sessionId);
            records.replace(account,sessions);
        } else {
            HashSet<String> sessions = new HashSet<>();
            sessions.add(sessionId);
            records.put(account,sessions);
        }
    }

    public  HashSet<String> getSessionIds(String account){
        return this.records.get(account);
    }

    public  void removeSessionId(String account, String sessionId){
        HashSet<String> sessions = records.get(account);
        if (sessions != null){
            sessions.remove(sessionId);
            records.replace(account,sessions);
        }
    }

    public  void removeAccount(String account){
        this.records.remove(account);
    }

    public boolean containsAccount(String account){
        return records.containsKey(account);
    }

}
