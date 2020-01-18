package eosio.spectrum.websocket.api.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:global.properties")
@ConfigurationProperties
public class Properties {

    @Value("${CHRONICLE_WEBSOCKET_PATH}")
    private String socketPath;

    @Value("${CUSTOMER_WEBSOCKET_PATH}")
    private String socketPathFrontend;

    @Value("${REDIS_HOSTNAME}")
    private String redisHostname;

    public String getSocketPath() {
        return socketPath;
    }

    public void setSocketPath(String socketPath) {
        this.socketPath = socketPath;
    }

    public String getSocketPathFrontend() {
        return socketPathFrontend;
    }

    public void setSocketPathFrontend(String socketPathFrontend) {
        this.socketPathFrontend = socketPathFrontend;
    }

    public String getRedisHostname() {
        return redisHostname;
    }

    public void setRedisHostname(String redisHostname) {
        this.redisHostname = redisHostname;
    }
}
