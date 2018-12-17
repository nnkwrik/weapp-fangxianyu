package io.github.nnkwrik.common.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 注册用户的消息队列
 *
 * @author nnkwrik
 * @date 18/11/19 16:19
 */
public interface UserRegisterStream {

    String INPUT = "register-input";
    String OUTPUT = "register-output";

    @Input(UserRegisterStream.INPUT)
    SubscribableChannel input();

    @Output(UserRegisterStream.OUTPUT)
    MessageChannel output();

}
