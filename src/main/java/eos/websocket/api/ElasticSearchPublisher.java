package eos.websocket.api;

import eos.websocket.api.configuration.Properties;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

@Service
public class ElasticSearchPublisher implements ElasticSearchPublisherInterface {
    private static final transient Logger logger = LoggerFactory.getLogger(ElasticSearchPublisher.class);
    private RestHighLevelClient restHighLevelClient;
    private RestClient restClient;
    private String ES_TRASNPORT_HOST1;
    private String ES_TRASNPORT_HOST2;
    private String ES_CLUSTER_NAME;
    private String ACTIONS_INDEX;
    private String TRANSACTION_INDEX;
    private Settings settings;
    private BulkProcessor bulkProcessor;
    private TransportClient client;

    @Autowired
        public ElasticSearchPublisher(Properties properties){
        ES_CLUSTER_NAME = properties.getEsClusterName();
        ES_TRASNPORT_HOST1 = properties.getEsTransportHost1();
        ES_TRASNPORT_HOST2 = properties.getEsTransportHost2();
        ACTIONS_INDEX = properties.getActionsIndex();
        TRANSACTION_INDEX = properties.getTransactionIndex();
        logger.info("Constructor es cluser name is: " + ES_CLUSTER_NAME + " transport host 1 is: " + ES_TRASNPORT_HOST1 + " ES_TRANSPORT_HOST2 " + ES_TRASNPORT_HOST2);
        this.settings = Settings.builder().put("cluster.name", ES_CLUSTER_NAME).build();

        try {
            this.client = new PreBuiltTransportClient(this.settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(ES_TRASNPORT_HOST1), 9300))
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(ES_TRASNPORT_HOST2), 9300));
        } catch (UnknownHostException e) {

            logger.info(e.toString());
        }

        this.bulkProcessor = BulkProcessor.builder(
                this.client,
                new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long executionId,
                                           BulkRequest bulkRequest) {
                        logger.info("bulk request numberOfActions:" + bulkRequest.numberOfActions());
                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest bulkRequest,
                                          BulkResponse bulkResponse) {
                        logger.info("bulk response has failures: " + bulkResponse.hasFailures());

                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          Throwable failure) {
                        logger.warn("bulk failed: " + failure);
                        logger.warn(failure.getMessage());
                        logger.warn("failure response: " + failure.getCause());


                    }
                })
                .setBulkActions(40000)
                .setGlobalType("_doc")
                .setBulkSize(new ByteSizeValue(25, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(40))
                .setConcurrentRequests(20)
                .setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();
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

}


