package eos.websocket.api;

import org.json.JSONObject;

import java.util.ArrayList;

public interface ElasticSearchPublisherInterface {
    void pubTransaction(JSONObject transaction);
    void pubActions(ArrayList<JSONObject> actions);
}
