package io.github.nnkwrik.authservice.util;

import com.auth0.jwt.interfaces.RSAKeyProvider;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author nnkwrik
 * @date 18/11/12 15:02
 */
public class RSAPvtKeyProvider implements RSAKeyProvider {
    private final RSAPrivateKey privateKey;

    public RSAPvtKeyProvider() {
        privateKey = RSAKeysUtil.readRsaPvt();
    }

    @Override
    public RSAPublicKey getPublicKeyById(String s) {
        return null;
    }

    @Override
    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    @Override
    public String getPrivateKeyId() {
        return null;
    }
}
