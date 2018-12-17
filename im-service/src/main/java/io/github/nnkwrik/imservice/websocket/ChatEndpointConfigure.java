package io.github.nnkwrik.imservice.websocket;

import io.github.nnkwrik.common.dto.JWTUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;
import java.util.Map;

/**
 * 自定义ServerEndpoint配置
 * 1. 使Endpoint能autowire spring的bean
 * 2. 获取Authorization头
 *
 * @author nnkwrik
 * @date 18/12/06 20:34
 */
@Slf4j
@Configuration
public class ChatEndpointConfigure extends ServerEndpointConfig.Configurator implements ApplicationContextAware {

    private static volatile BeanFactory context;

    /**
     * 在ChatEndpoint中autowire spring的Bean
     *
     * @param clazz
     * @param <T>
     * @return
     * @throws InstantiationException
     */
    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return context.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ChatEndpointConfigure.context = applicationContext;
    }

    /**
     * 在@onOpen中获取token
     *
     * @param config
     * @param request
     * @param response
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        Map<String, List<String>> headers = request.getHeaders();
        List<String> authHeader = headers.get("Authorization");
        if (authHeader == null) {
            config.getUserProperties().put(JWTUser.class.getName(), "");
        } else {
            config.getUserProperties().put(JWTUser.class.getName(), authHeader.get(0));
        }

    }


}
