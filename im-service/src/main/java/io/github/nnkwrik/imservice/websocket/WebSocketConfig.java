package io.github.nnkwrik.imservice.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author nnkwrik
 * @date 18/12/05 11:32
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public ChatEndpointConfigure newConfigure() {
        return new ChatEndpointConfigure();
    }
}
