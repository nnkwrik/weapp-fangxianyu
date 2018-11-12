package io.github.nnkwrik.authservice.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.nnkwrik.common.dto.JWTUser;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


/**
 * @author nnkwrik
 * @date 18/11/12 14:18
 */
public class TokenFactory {

    public static String createTokenFromUser(JWTUser jwtUser) {
        Algorithm algorithm = Algorithm.RSA256(new RSAPvtKeyProvider());
        //一天后过期
        Date expire = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        String token = JWT.create()
                .withClaim("openId", jwtUser.getOpenId())
                .withClaim("nickName", jwtUser.getNickName())
                .withClaim("avatarUrl", jwtUser.getAvatarUrl())
                .withExpiresAt(expire)
                .sign(algorithm);

        return "Bearer " + token;
    }

    public static JWTUser createUserFromToken(String token) {
        Algorithm algorithm = Algorithm.RSA256(new RSAPubKeyProvider());
        //会自动验证过期时间
        DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);
        JWTUser jwtUser = new JWTUser();

        jwtUser.setOpenId(decodedJWT.getClaim("openId").asString());
        jwtUser.setNickName(decodedJWT.getClaim("nickName").asString());
        jwtUser.setAvatarUrl(decodedJWT.getClaim("avatarUrl").asString());

        return jwtUser;
    }

}
