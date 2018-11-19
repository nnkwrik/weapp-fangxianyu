package io.github.nnkwrik.userservice.mq;

import io.github.nnkwrik.common.mq.UserRegisterStream;
import io.github.nnkwrik.common.util.JsonUtil;
import io.github.nnkwrik.userservice.model.User;
import io.github.nnkwrik.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * @author nnkwrik
 * @date 18/11/19 16:20
 */
@Slf4j
@Component
@EnableBinding(UserRegisterStream.class)
public class RegisterStreamReceiver {

    @Autowired
    private UserService userService;

    @StreamListener(target = UserRegisterStream.INPUT)
    public void receive(String userData){
        User user = JsonUtil.fromJson(userData, User.class);

        log.info("从 [ auth-service ] 收到 [ 用户注册 ]的消息");
        log.info("新用户：用户名 = [{}],城市 = [{}]", user.getNickName(), user.getCity());
        userService.register(user);
    }
}
