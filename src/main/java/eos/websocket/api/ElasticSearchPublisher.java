package eos.websocket.api;

import org.apache.http.HttpHost;
import org.apache.kafka.common.protocol.types.Field;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
@Component
public class ElasticSearchPublisher implements ElasticSearchPublisherInterface{
        private static final transient Logger logger = LoggerFactory.getLogger(ElasticSearchPublisher.class);
        private RestHighLevelClient restHighLevelClient;
        private BulkProcessor bulkProcessor;
        private TransportClient client;
        private RestClient restClient;
        private String actionsIndex;
        private String transactionIndex;

        private String ES_TRASNPORT_HOST1;
        private String ES_TRASNPORT_HOST2;
        private String ES_CLUSTER_NAME;

    @Bean
    public ElasticSearchPublisher elasticSearchPublisher(){
        return new ElasticSearchPublisher();
    }

    @Autowired
    public void setProperties(Properties properties) {
        ES_CLUSTER_NAME = properties.getEsClusterName();
        ES_TRASNPORT_HOST1 = properties.getEsTransportHost1();
        ES_TRASNPORT_HOST2 = properties.getEsTransportHost2();
        logger.info("es cluser name is: "+ES_CLUSTER_NAME+" transport host 1 is: "+ES_TRASNPORT_HOST1);
        logger.info(this.toString());
        Settings settings = Settings.builder()
                .put("cluster.name", ES_CLUSTER_NAME).build();
//        this.client = new RestClient.builder();
        try {
            this.client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(ES_TRASNPORT_HOST1), 9300))
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(ES_TRASNPORT_HOST2), 9300));
            logger.info("Constructor es cluser name is: "+ES_CLUSTER_NAME+" transport host 1 is: "+ES_TRASNPORT_HOST1);
        }catch (UnknownHostException e){


        }
        this.bulkProcessor = BulkProcessor.builder(
                client,
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
                        logger.warn("failure response: "+failure.getCause());


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
            bulkProcessor.add(new IndexRequest(this.actionsIndex).source(action.toString(), XContentType.JSON));
        }
    }
    @Override
    public void pubTransaction(JSONObject transaction){
             bulkProcessor.add(new IndexRequest(this.transactionIndex).source(transaction.toString(), XContentType.JSON));
    }

}


