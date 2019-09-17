package eosio.spectrum.websocket.api;

import com.google.gson.Gson;
import eosio.spectrum.websocket.api.Message.chronicle.ActionTraces;
import eosio.spectrum.websocket.api.Message.chronicle.Transaction;
import eosio.spectrum.websocket.api.configuration.Properties;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;


@Service
public class ElasticSearchPublisher {
    private static final transient Logger logger = LoggerFactory.getLogger(ElasticSearchPublisher.class);
    private RestHighLevelClient restHighLevelClient;
    private String ACTIONS_INDEX;
    private String TRANSACTION_INDEX;
    private String NEW_ACCOUNT_INDEX;
    private String TRANSFER_INDEX;
    private BulkProcessor bulkProcessor;
    private Boolean failureState;



    @Autowired
        public ElasticSearchPublisher(Properties properties){

        failureState = false;
        ACTIONS_INDEX = properties.getActionsIndex();
        TRANSACTION_INDEX = properties.getTransactionIndex();
        NEW_ACCOUNT_INDEX = properties.getNewAccountIndex();
        TRANSFER_INDEX = properties.getTransferIndex();

        logger.debug("ES_HOST1 is: " + properties.getEsHost1() + " ES_HOST2 " + properties.getEsHost2());
        this.restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(properties.getEsHost1(), 9200, "http"),
                        new HttpHost(properties.getEsHost2(), 9200, "http")));

        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest bulkRequest) {
                logger.info("bulk request numberOfActions:" + bulkRequest.numberOfActions());
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request,
                                  BulkResponse bulkResponse) {
                logger.info("bulk response has failures: " + bulkResponse.hasFailures());

            }

            @Override
            public void afterBulk(long executionId, BulkRequest request,
                                  Throwable failure) {

                setFailureState(true);
                logger.warn("bulk failed: " + failure);
                logger.warn(failure.getMessage());
                logger.warn("failure response: " + failure.getCause());

            }
        };

        this.bulkProcessor = BulkProcessor.builder(
                (request, bulkListener) ->
                        restHighLevelClient.bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
                listener).
                setBulkActions(40000)
                .setGlobalType("_doc").setBulkSize(new ByteSizeValue(25, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(90))
                .setConcurrentRequests(20)
                .setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();

    }


    public Boolean getFailureState() {
        return failureState;
    }

    public void setFailureState(Boolean failureState) {
        this.failureState = failureState;
    }

    public void pubActions(List<ActionTraces> actions){
        for (ActionTraces action: actions) {
            String json = new Gson().toJson(action);
            bulkProcessor.add(new IndexRequest(this.ACTIONS_INDEX).source(json, XContentType.JSON));
        }
    }
    public void pubTransaction(Transaction transaction){
        String json = new Gson().toJson(transaction);
        bulkProcessor.add(new IndexRequest(this.TRANSACTION_INDEX).source(json, XContentType.JSON));
    }

    public void pubNewAccountActions(List<ActionTraces> actions){
        for (ActionTraces action: actions) {
            String json = new Gson().toJson(action);
            bulkProcessor.add(new IndexRequest(this.NEW_ACCOUNT_INDEX).source(json, XContentType.JSON));
        }
    }

    public void pubTransferActions(List<ActionTraces> actions){
        for (ActionTraces action: actions)
        {
            String json = new Gson().toJson(action);
            bulkProcessor.add(new IndexRequest(this.TRANSFER_INDEX).source(json, XContentType.JSON));
        }

    }
}


