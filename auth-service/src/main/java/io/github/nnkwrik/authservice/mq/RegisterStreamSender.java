package io.github.nnkwrik.authservice.mq;

import io.github.nnkwrik.common.mq.UserRegisterStream;
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
public class RegisterStreamSender {

    @Autowired
    private UserRegisterStream streamClient;

    public void send(String userDate) {
        streamClient.output().send(MessageBuilder.withPayload(userDate).build());
    }
}
