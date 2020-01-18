package eosio.spectrum.websocket.api.configuration;

import eosio.spectrum.websocket.api.SocketHandler;
import eosio.spectrum.websocket.api.SocketHandlerFrontend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;


@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    private SocketHandler socketHandler;

    private SocketHandlerFrontend socketHandlerFrontend;

    private String socketPath;
    private String socketPathFrontend;

    @Autowired
    public void setSocketPath(Properties properties) {
        this.socketPath = properties.getSocketPath();
    }

    @Autowired
    void setSocketPathFrontend(Properties properties) {
        this.socketPathFrontend = properties.getSocketPathFrontend();
    }

    @Autowired
    public void setSocketHandler(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    @Autowired
    public void setSocketHandlerFrontend(SocketHandlerFrontend socketHandlerFrontend) {
        this.socketHandlerFrontend = socketHandlerFrontend;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler, socketPath);
        registry.addHandler(socketHandlerFrontend, socketPathFrontend).
                setAllowedOrigins("*");
    }


    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(9500000);
        container.setMaxBinaryMessageBufferSize(9500000);
//        WebSocketTransportRegistration webSocketTransportRegistration = new WebSocketTransportRegistration();
//        webSocketTransportRegistration.setMessageSizeLimit();
        return container;
    }


}