package eosio.spectrum.websocket.api.redis.publishers;

import eosio.spectrum.websocket.api.MessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class RedisMessagePublisherBlocks implements MessagePublisher {


    private final StringRedisTemplate stringRedisTemplate;
    private final ChannelTopic topicBlocks;

    @Autowired
    public RedisMessagePublisherBlocks(StringRedisTemplate stringRedisTemplate,
                                       ChannelTopic topicBlocks) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.topicBlocks = topicBlocks;
    }

    @Override
    public void publish(String message) {
        stringRedisTemplate.convertAndSend(topicBlocks.getTopic(), message);
    }
}
