package eosio.spectrum.websocket.api;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eosio.spectrum.websocket.api.message.eosio.ActData;
//import eosio.spectrum.websocket.api.message.eosio.Trx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

public class ActDataDeserializer implements JsonDeserializer<ActData> {

    private static final transient Logger logger = LoggerFactory.getLogger(ActDataDeserializer.class);


    @Override
    public ActData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
         ActData actData = new ActData();
        logger.info(json.toString());
        return actData;
    }
}
