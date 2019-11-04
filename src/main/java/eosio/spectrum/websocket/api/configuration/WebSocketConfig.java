package eosio.spectrum.websocket.api.configuration;

import eosio.spectrum.websocket.api.SocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;


@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    private String websocketPath;
    private SocketHandler socketHandler;

    @Autowired
    public void setWebsocketPath(Properties properties){
        this.websocketPath = properties.getWebsocketPath();

    }

    @Autowired
    public void setSocketHandler(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler, this.websocketPath);
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(195500000);
        container.setMaxBinaryMessageBufferSize(595500000);
        return container;
    }

}
