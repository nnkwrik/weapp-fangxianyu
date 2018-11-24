package io.github.nnkwrik.authservice.jwt;

import io.github.nnkwrik.authservice.token.TokenCreator;
import io.github.nnkwrik.common.dto.JWTUser;
import io.github.nnkwrik.common.token.TokenSolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author nnkwrik
 * @date 18/11/18 22:20
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestToken {

    @Value("${jwt.pub-key-file-name}")
    private String pubFile;

    @Value("${jwt.pvt-key-file-name}")
    private String pvtFile;

    @Autowired
    private TokenCreator creator;

    @Autowired
    private TokenSolver solver;


    @Test
    public void testToken() throws Exception {
        //测试注入密钥文件名
        System.out.println(pubFile);
        System.out.println(pvtFile);

        JWTUser jwtUser = new JWTUser();
        jwtUser.setOpenId("1212");
        jwtUser.setNickname("nickName");


        //找到resource目录下的私钥文件后生成Token
        String token = creator.create(jwtUser);
        System.out.println(token);

        token = token.replace("Bearer ", "");

        //找到resource目录下的公钥文件后还原用户信息
        JWTUser user = solver.solve(token);
        System.out.println(user);

    }

}
