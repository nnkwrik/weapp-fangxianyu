package io.github.nnkwrik.authservice.mq;

import io.github.nnkwrik.common.mq.UserRegisterStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author nnkwrik
 * @date 18/11/19 21:36
 */
@Component
@EnableBinding(UserRegisterStream.class)
@Slf4j
public class RegisterStreamSender {

    @Autowired
    private UserRegisterStream streamClient;

    /**
     * 通过消息队列注册用户
     *
     * @param userDate
     */
    public void send(String userDate) {
        log.info("向【用户服务】发起【用户注册】的消息，消息内容：{}", userDate);
        streamClient.output().send(MessageBuilder.withPayload(userDate).build());
    }
}
