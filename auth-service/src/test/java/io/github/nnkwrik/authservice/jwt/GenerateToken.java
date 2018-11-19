package io.github.nnkwrik.authservice.jwt;

import io.github.nnkwrik.authservice.token.TokenCreator;
import io.github.nnkwrik.common.dto.JWTUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author nnkwrik
 * @date 18/11/19 9:47
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GenerateToken {

    @Autowired
    private TokenCreator creator;

    /**
     * 生产环境下可以用这个生成Token进行测试
     */
    @Test
    public void generateToken(){
        String openId = "1212";
        String nickName = "nickname";
        String avatarUrl = "https://avatars2.githubusercontent.com/u/29662114?s=460&v=4";

        JWTUser jwtUser = new JWTUser(openId,nickName,avatarUrl);
        String token = creator.create(jwtUser);

        System.out.println("==================== 生成的Token =========================\n");
        System.out.println(token);
        System.out.println("\n=========================================================");

    }
}
