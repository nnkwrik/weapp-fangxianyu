package io.github.nnkwrik.common.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import io.github.nnkwrik.common.dto.JWTUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author nnkwrik
 * @date 18/11/18 21:27
 */
@Component
public class TokenSolver {

    @Value("${jwt.pub-key-file-name}")
    private String pubFile;

    private RSAKeyProvider keyProvider = new RSAKeyProvider() {

        RSAPublicKey key;

        @Override
        public RSAPublicKey getPublicKeyById(String s) {
            if (key == null) key = RSAKeysReader.readRsaPub(pubFile);
            return key;
        }

        @Override
        public RSAPrivateKey getPrivateKey() {
            return null;
        }

        @Override
        public String getPrivateKeyId() {
            return null;
        }
    };

    public JWTUser solve(String token) {
        token = token.replace("Bearer ", "");
        Algorithm algorithm = Algorithm.RSA256(keyProvider);
        //会自动验证过期时间
        DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);
        JWTUser jwtUser = new JWTUser();

        jwtUser.setOpenId(decodedJWT.getClaim("openId").asString());
        jwtUser.setNickName(decodedJWT.getClaim("nickName").asString());
        jwtUser.setAvatarUrl(decodedJWT.getClaim("avatarUrl").asString());

        return jwtUser;
    }
}
