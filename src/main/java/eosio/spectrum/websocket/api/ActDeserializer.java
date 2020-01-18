package eosio.spectrum.websocket.api;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eosio.spectrum.websocket.api.message.eosio.Act;
import eosio.spectrum.websocket.api.message.eosio.Authorization;
import eosio.spectrum.websocket.api.message.eosio.Trx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ActDeserializer implements JsonDeserializer<Act> {
    private Act act;
    private List<Authorization> authorizations;
    private Authorization authorization;
    private String name;
    private String account;
private String data;
    private static final transient Logger logger = LoggerFactory.getLogger(ActDeserializer.class);


    @Override
    public Act deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        act = new Act();
        authorizations = new ArrayList<>();

        for (JsonElement jsonElement:json.getAsJsonObject().getAsJsonArray("authorization")) {
            authorization = new Authorization();
            String actor = jsonElement.getAsJsonObject().get("actor").getAsString();
            String permission = jsonElement.getAsJsonObject().get("permission").getAsString();
            authorization.setActor(actor);
            authorization.setPermission(permission);
            authorizations.add(authorization);
        }

        name = json.getAsJsonObject().get("name").getAsString();
        account = json.getAsJsonObject().get("account").getAsString();
        data = json.getAsJsonObject().get("data").toString();
        act.setAuthorization(authorizations);
        act.setAccount(account);
        act.setName(name);
        act.setData(data);
        return act;
    }
}
