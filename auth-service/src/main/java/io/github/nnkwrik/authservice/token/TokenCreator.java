package io.github.nnkwrik.authservice.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import io.github.nnkwrik.common.dto.JWTUser;
import io.github.nnkwrik.common.token.RSAKeysReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


/**
 * @author nnkwrik
 * @date 18/11/12 14:18
 */
@Component
public class TokenCreator {

    @Value("${jwt.pvt-key-file-name}")
    private String pvtFile;

    private RSAKeyProvider keyProvider = new RSAKeyProvider() {

        RSAPrivateKey key;

        @Override
        public RSAPublicKey getPublicKeyById(String s) {
            return null;
        }

        @Override
        public RSAPrivateKey getPrivateKey() {
            if (key == null) key = RSAKeysReader.readRsaPvt(pvtFile);
            return key;
        }

        @Override
        public String getPrivateKeyId() {
            return null;
        }
    };

    public String create(JWTUser jwtUser) {
        Algorithm algorithm = Algorithm.RSA256(keyProvider);
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

    public String create(String openId, String nickName, String avatarUrl) {
        Algorithm algorithm = Algorithm.RSA256(keyProvider);
        //一天后过期
        Date expire = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        String token = JWT.create()
                .withClaim("openId", openId)
                .withClaim("nickName", nickName)
                .withClaim("avatarUrl", avatarUrl)
                .withExpiresAt(expire)
                .sign(algorithm);

        return "Bearer " + token;
    }

}
