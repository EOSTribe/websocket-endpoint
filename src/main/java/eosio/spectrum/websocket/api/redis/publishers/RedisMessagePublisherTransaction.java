package eosio.spectrum.websocket.api.redis.publishers;

import eosio.spectrum.websocket.api.MessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class RedisMessagePublisherTransaction implements MessagePublisher {


    private final StringRedisTemplate stringRedisTemplate;
    private final ChannelTopic topicTransaction;

    @Autowired
    public RedisMessagePublisherTransaction(StringRedisTemplate stringRedisTemplate,
                                            ChannelTopic topicTransaction) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.topicTransaction = topicTransaction;
    }

    @Override
    public void publish(String message) {
        stringRedisTemplate.convertAndSend(topicTransaction.getTopic(), message);
    }
}
