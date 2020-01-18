package eosio.spectrum.websocket.api.configuration;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig implements ApplicationContextAware {

    private final String ACTIONS_CHANNEL = "actions";
    private final String SERVICE_CHANNEL = "service";
    private final String TRANSACTION_CHANNEL = "transaction";
    private final String BLOCKS_CHANNEL = "blocks";
    private final String TABLE_ROWS = "tbl_rows";

    private String redisHostname;

    private ApplicationContext applicationContext = null;

    @Autowired
    public void setRedisHostname(Properties properties) {
        this.redisHostname = properties.getRedisHostname();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHostname);
        redisStandaloneConfiguration.setPort(6379);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
        return jedisConnectionFactory;
    }

    @Bean
    public StringRedisTemplate redisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    @Bean
    public MessageListenerAdapter messageListenerServiceHandler() {
        return new MessageListenerAdapter(applicationContext.getBean("messageListenerService"));
    }
    @Bean
    public MessageListenerAdapter messageListenerActionsHandler() {
        return new MessageListenerAdapter(applicationContext.getBean("messageListenerActions"));
    }
    @Bean
    public MessageListenerAdapter messageListenerTransactionHandler() {
        return new MessageListenerAdapter(applicationContext.getBean("messageListenerTransaction"));
    }
    @Bean
    public MessageListenerAdapter messageListenerBlocksHandler() {
        return new MessageListenerAdapter(applicationContext.getBean("messageListenerBlocks"));
    }

    @Bean
    public MessageListenerAdapter messageListenerTableRowsHandler() {
        return new MessageListenerAdapter(applicationContext.getBean("messageListenerTableRows"));
    }

    @Bean
    public RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(messageListenerServiceHandler(), topicService());
        container.addMessageListener(messageListenerActionsHandler(), topicActions());
        container.addMessageListener(messageListenerTransactionHandler(), topicTransaction());
        container.addMessageListener(messageListenerBlocksHandler(), topicBlocks());
        container.addMessageListener(messageListenerTableRowsHandler(), topicTableRows());
        return container;
    }

    @Bean
    public ChannelTopic topicActions() {
        return new ChannelTopic(ACTIONS_CHANNEL);
    }

    @Bean
    public ChannelTopic topicService() {
        return new ChannelTopic(SERVICE_CHANNEL);
    }

    @Bean
    public ChannelTopic topicTransaction() {
        return new ChannelTopic(TRANSACTION_CHANNEL);
    }

    @Bean
    public ChannelTopic topicBlocks(){
        return new ChannelTopic(BLOCKS_CHANNEL);
    }

    @Bean
    public ChannelTopic topicTableRows(){
        return new ChannelTopic(TABLE_ROWS);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
