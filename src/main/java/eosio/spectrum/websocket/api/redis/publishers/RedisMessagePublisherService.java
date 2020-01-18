package eosio.spectrum.websocket.api.redis.publishers;

import eosio.spectrum.websocket.api.MessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class RedisMessagePublisherService implements MessagePublisher {


    private final StringRedisTemplate stringRedisTemplate;
    private final ChannelTopic topicService;

    @Autowired
    public RedisMessagePublisherService(StringRedisTemplate stringRedisTemplate,
                                        ChannelTopic topicService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.topicService = topicService;
    }

    @Override
    public void publish(String message) {
        stringRedisTemplate.convertAndSend(topicService.getTopic(), message);
    }
}
