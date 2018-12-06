package io.github.nnkwrik.imservice.websocket;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.websocket.server.ServerEndpointConfig;

/**不自定义就无法在ChatEndpoint中autowire spring的Bean
 * @author nnkwrik
 * @date 18/12/06 20:34
 */
public class ChatEndpointConfigure extends ServerEndpointConfig.Configurator implements ApplicationContextAware {
    private static volatile BeanFactory context;

    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return context.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("auto load" + this.hashCode());
        ChatEndpointConfigure.context = applicationContext;
    }
}
