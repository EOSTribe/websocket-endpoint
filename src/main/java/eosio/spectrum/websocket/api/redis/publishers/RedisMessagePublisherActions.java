package eosio.spectrum.websocket.api.redis.publishers;

import eosio.spectrum.websocket.api.MessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class RedisMessagePublisherActions implements MessagePublisher {


    private final StringRedisTemplate stringRedisTemplate;
    private final ChannelTopic topicActions;

    @Autowired
    public RedisMessagePublisherActions(StringRedisTemplate stringRedisTemplate,
                                        ChannelTopic topicActions) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.topicActions = topicActions;
    }

    @Override
    public void publish(String message) {
        stringRedisTemplate.convertAndSend(topicActions.getTopic(), message);
    }
}
