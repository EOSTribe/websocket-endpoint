package eos.websocket.api;

import org.apache.http.HttpHost;
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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ElasticSearchPublisher {
        private static final transient Logger logger = LoggerFactory.getLogger(ElasticSearchPublisher.class);
        private RestHighLevelClient restHighLevelClient;
        private BulkProcessor bulkProcessor;
        private TransportClient client;
        private RestClient restClient;


    public ElasticSearchPublisher() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", "eostribe-es-02").build();
//        this.client = new RestClient.builder();
        this.client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("es-test.eostribe.io"), 9300))
                .addTransportAddress(new TransportAddress(InetAddress.getByName("test2.eostribe.io"), 9300));


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
                .setBulkActions(20000)
                .setGlobalType("_doc")
                .setBulkSize(new ByteSizeValue(25, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setConcurrentRequests(5)
                .setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();

    }

    public void pingElasticsearch(){


    }


    public void pubActions(ArrayList<JSONObject> actions){

        for (JSONObject action: actions
             ) {
            bulkProcessor.add(new IndexRequest("test-mainet-actions").source(action.toString(), XContentType.JSON));
        }

    }

    public void pubTransaction(JSONObject transaction){
             bulkProcessor.add(new IndexRequest("test-mainet-transaction").source(transaction.toString(), XContentType.JSON));
    }

}


