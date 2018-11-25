package io.github.nnkwrik.authservice.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import io.github.nnkwrik.common.dto.JWTUser;
import io.github.nnkwrik.common.token.RSAKeysReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
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

    public RSAKeyProvider keyProvider = new RSAKeyProvider() {

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

    public String create(JWTUser jwtUser) throws IllegalAccessException {
        Algorithm algorithm = Algorithm.RSA256(keyProvider);
        //一天后过期
        Date expire = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

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
