package io.github.nnkwrik.goodsservice.controller;

import io.github.nnkwrik.common.dto.JWTUser;
import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.token.TokenSolver;
import io.github.nnkwrik.common.token.injection.JWT;
import io.github.nnkwrik.goodsservice.client.UserClient;
import io.github.nnkwrik.goodsservice.model.po.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author nnkwrik
 * @date 18/11/23 22:24
 */
@RestController
public class TestController {

    @Autowired
    private UserClient client;

    @GetMapping("/testFeign")
    public Response testFeign() {
        Map<String, String> map = new HashMap<>();
        map.put("openId", "1");
        return client.getSimpleUser(map);
    }

    @GetMapping("/testFeign2")
    public Response testFeign2() {
        List<String> ids = new ArrayList<>();
        ids.add("1");
        ids.add("3");
        return client.getSimpleUserList(ids);
    }

    @GetMapping("/testFeign3")
    public Response testFeign3() {
        return client.getSimpleUser("1");
    }


    /**
     * jwt解析失败时会抛错
     * @param jwt
     * @return
     */
    @GetMapping("/testInjection")
    public JWTUser testInject(@JWT JWTUser jwt) {

        System.out.println("============" + jwt);
        return jwt;
    }

    /**
     * jwt解析失败时不会抛错
     * @param jwt 解析失败时是null
     * @return
     */
    @GetMapping("/testInjection2")
    public JWTUser testInject2(@JWT(required = false) JWTUser jwt) {

        System.out.println("============" + jwt);
        return jwt;
    }

    @Autowired
    private TokenSolver tokenSolver;

    @GetMapping("/testJWTSolver")
    public Object testJWTSolver() throws Exception {
//        String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJvcGVuSWQiOiIxIiwibmlja25hbWUiOiLmtYvor5XnlKjmiLciLCJhdmF0YXIiOiJodHRwczovL2F2YXRhcnMyLmdpdGh1YnVzZXJjb250ZW50LmNvbS91LzI5NjYyMTE0P3M9NDYwJnY9NCIsImV4cCI6MTU0MzExMDM1OX0.WnlQL2so0oHU7gUn-V0ij-uACizN7kQRBTwowZxwtvTMGw3_qE6ElJjzGUrRQDGZrad6eKxSf7m1v_ptz4UBoKbzickPGei-Qo9AkZZt0wL77HxislXkJltKHl3BgGbSdF2SkyARAPrlKyFy60aHEUw2dv6UbaDbJC_mUlBGwukyjOqCg5C4KNEgvnFiWYpHEQGKq3L12y1ba2VhtBDnrqAhKFjWvxiU5zM5ei5uDXaUhhUEvICJ1ariR9ZiLIbUcJ_CP63bVF3-yglTeCmZVmoddE0Cqq5d0nTDq3VozkIi74rFiK4oSK3wF8D_r9MeAzK1X-BUkkTkg-qV0_Vgjw";
        String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJvcGVuSWQiOiIxIiwibmlja25hbWUiOiLmtYvor5XnlKjmiLciLCJhdmF0YXIiOiJodHRwczovL2F2YXRhcnMyLmdpdGh1YnVzZXJjb250ZW50LmNvbS91LzI5NjYyMTE0P3M9NDYwJnY9NCIsImV4cCI6MTU0MzExMTg1MX0.Vf27ms7mRHOg8ptM0NTvgj5umfKae4Hk1lFuUmMjA4gdNLuhlqObp24JDsGfc4jo9yBKbwEdQ7DYOeKrFLybEArNXKZW2qf4XVUKIp2uLTlCw5n8BMSh13gPECjgfUInrMhEFPEB3IiJEhPDhQdiiRSYV4bQcCIS4d31OodqBTXDG0PC-FHc_7tgg3Xm7_ACLgI8JKOXmaSYPBrwGpQpu12-xXgR5DzjGR-TBICH8cY8SfkRexN6WriAYoGqJYFPKkkcnwKS4R6NzD_LGmHsCJ_XQjX5iWCtVz8SSL9tYS6E66NJDRFMWNkvDuwpuR9nefxv_zeqMJOcm6FVEYGZEw";
        JWTUser solve = tokenSolver.solve(token);
        System.out.println(solve);
        return solve;
    }

    //testNOT_NULL_JSON
    @GetMapping("/testJson")
    public Object testNOT_NULL_JSON(){
        Goods goods = new Goods();
        goods.setId(1);
        goods.setLastEdit(new Date());
        return goods;
    }
}
