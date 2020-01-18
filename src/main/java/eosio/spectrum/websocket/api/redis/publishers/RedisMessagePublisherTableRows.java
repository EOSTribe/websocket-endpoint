package eosio.spectrum.websocket.api.redis.publishers;

import eosio.spectrum.websocket.api.MessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class RedisMessagePublisherTableRows implements MessagePublisher {

    private final StringRedisTemplate stringRedisTemplate;
    private final ChannelTopic topicTableRows;

    @Autowired
    public RedisMessagePublisherTableRows(StringRedisTemplate stringRedisTemplate,
                                          ChannelTopic topicTableRows) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.topicTableRows = topicTableRows;
    }

    @Override
    public void publish(String message) {
        stringRedisTemplate.convertAndSend(topicTableRows.getTopic(), message);
    }
}
