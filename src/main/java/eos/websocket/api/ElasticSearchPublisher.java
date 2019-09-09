package eos.websocket.api;

import eos.websocket.api.configuration.Properties;
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


@Service
public class ElasticSearchPublisher implements ElasticSearchPublisherInterface {
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

    @Override
    public void pubActions(ArrayList<JSONObject> actions){
        for (JSONObject action: actions) {
            bulkProcessor.add(new IndexRequest(this.ACTIONS_INDEX).source(action.toString(), XContentType.JSON));
        }
    }
    @Override
    public void pubTransaction(JSONObject transaction){
             bulkProcessor.add(new IndexRequest(this.TRANSACTION_INDEX).source(transaction.toString(), XContentType.JSON));
    }

    public void pubNewAccountActions(ArrayList<JSONObject> actions){
        for (JSONObject action: actions) {
            bulkProcessor.add(new IndexRequest(this.NEW_ACCOUNT_INDEX).source(action.toString(), XContentType.JSON));
        }
    }

    public void pubTransferActions(ArrayList<JSONObject> actions){
        for (JSONObject action: actions) {
            bulkProcessor.add(new IndexRequest(this.TRANSFER_INDEX).source(action.toString(), XContentType.JSON));
        }
    }
}


