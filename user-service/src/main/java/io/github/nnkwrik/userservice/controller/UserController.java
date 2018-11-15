package io.github.nnkwrik.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.nnkwrik.common.dto.AuthDTO;
import io.github.nnkwrik.userservice.client.AuthClient;
import io.github.nnkwrik.userservice.model.User;
import io.github.nnkwrik.userservice.service.UserService;
import io.github.nnkwrik.userservice.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author nnkwrik
 * @date 18/11/10 21:51
 */
@RestController
public class UserController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private AuthClient authClient;

    @GetMapping("/test")
    public String test(){
        return authClient.test();
    }


    @PostMapping("/register")
    public Response register(@RequestBody AuthDTO authDTO) {

        Response response = authClient.login(authDTO);
        if (!response.isSuccess()) {
            log.info("认证失败,原因 ：{}", response.getMsg());
            return response;
        }

        //rawData转User
        ObjectMapper mapper = new ObjectMapper();
        User user = null;
        try {
            user = mapper.readValue(authDTO.getRawData(), User.class);
        } catch (IOException e) {
            log.info("rawData转User失败 ： rawData = {}", authDTO.getRawData());
            e.printStackTrace();
            return Response.fail("rawData转User失败 ");
        }
        userService.register(user);

        log.info("新用户：用户名 = [{}],城市 = [{}]", user.getNickName(), user.getCity());
        return response;
    }



}
