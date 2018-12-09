package io.github.nnkwrik.authservice.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import io.github.nnkwrik.authservice.token.TokenCreator;
import io.github.nnkwrik.common.dto.JWTUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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
    public void generateToken() throws IllegalAccessException {
        String openId = "3";
        String nickName = "测试用户";
        String avatarUrl = "https://avatars2.githubusercontent.com/u/29662114?s=460&v=4";

        JWTUser jwtUser = new JWTUser(openId, nickName, avatarUrl);
        String token = creator.create(jwtUser);

        System.out.println("==================== 生成的Token =========================\n");
        System.out.println(token);
        System.out.println("\n=========================================================");

    }

    @Test
    public void generateExpiredToken() throws IllegalAccessException {
        String openId = "1";
        String nickName = "测试用户";
        String avatarUrl = "https://avatars2.githubusercontent.com/u/29662114?s=460&v=4";

        JWTUser jwtUser = new JWTUser(openId, nickName, avatarUrl);
        String token = createExpiredToken(jwtUser);

        System.out.println("==================== 生成的Token =========================\n");
        System.out.println(token);
        System.out.println("\n=========================================================");
    }


    private String createExpiredToken(JWTUser jwtUser) throws IllegalAccessException {
        Algorithm algorithm = Algorithm.RSA256(creator.keyProvider);
        //一秒后过期
        Date expire = Date.from(Instant.now().plus(1, ChronoUnit.SECONDS));

        JWTCreator.Builder builder = JWT.create();

        //通过反射构造token字符串
        for (Field field : jwtUser.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String value = (String) field.get(jwtUser);
            String name = field.getName();
            builder.withClaim(name, value);
        }
        String token = builder.withExpiresAt(expire).sign(algorithm);

        return "Bearer " + token;
    }
}
