package eosio.spectrum.websocket.api;

import com.google.gson.*;
import eosio.spectrum.websocket.api.message.eosio.Transactions;
import eosio.spectrum.websocket.api.message.eosio.Trx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

public class TrxDeserializer implements JsonDeserializer<Trx> {
    private Trx trx;

    private static final transient Logger logger = LoggerFactory.getLogger(TrxDeserializer.class);


    @Override
    public Trx deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
         trx = new Trx();
        if (json.isJsonPrimitive())
        {
            String id = json.getAsJsonPrimitive().getAsString();
            trx.setId(id);
        } else{
            JsonObject jsonObject = json.getAsJsonObject();
            String signatures = jsonObject.get("signatures").toString();
            String compression = jsonObject.get("signatures").getAsString();
            String packed_context_free_data = jsonObject.get("packed_context_free_data").getAsString();
            String packed_trx = jsonObject.get("packed_trx").getAsString();
            trx.setSignatures(signatures);
            trx.setCompression(compression);
            trx.setPacked_context_free_data(packed_context_free_data);
            trx.setPacked_trx(packed_trx);
        }
        return trx;
    }
}
