package io.github.nnkwrik.authservice.util;

import com.auth0.jwt.interfaces.RSAKeyProvider;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author nnkwrik
 * @date 18/11/12 15:02
 */
public class RSAPubKeyProvider implements RSAKeyProvider {
    private final RSAPublicKey publicKey;

    public RSAPubKeyProvider() {
        publicKey = RSAKeysUtil.readRsaPub();
    }

    @Override
    public RSAPublicKey getPublicKeyById(String s) {
        return publicKey;
    }

    @Override
    public RSAPrivateKey getPrivateKey() {
        return null;
    }

    @Override
    public String getPrivateKeyId() {
        return null;
    }
}
