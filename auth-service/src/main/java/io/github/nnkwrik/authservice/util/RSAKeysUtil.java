package io.github.nnkwrik.authservice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author nnkwrik
 * @date 18/11/12 11:35
 */
public class RSAKeysUtil {

    private static final Logger log = LoggerFactory.getLogger(RSAKeysUtil.class);

    public static final String defaultLocation = "auth-service/src/main/resources";

    public static final String defaultFilePrefix = "RSA";

    public static void generateRSAKeys() {
        generateRSAKeys(defaultLocation, defaultFilePrefix);
    }

    public static void generateRSAKeys(String location, String filePrefix) {
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        PrivateKey pvt = kp.getPrivate();
        PublicKey pub = kp.getPublic();


        Base64.Encoder encoder = Base64.getEncoder();
        String outFile = location + "/" + filePrefix;

        Writer out = null;
        try {
            if (outFile != null) out = new FileWriter(outFile + ".key");
            else out = new OutputStreamWriter(System.out);

            System.err.println("Private key format: " +
                    kp.getPrivate().getFormat());
            out.write("-----BEGIN RSA PRIVATE KEY-----\n");
            out.write(encoder.encodeToString(pvt.getEncoded()));
            out.write("\n-----END RSA PRIVATE KEY-----\n");

            if (outFile != null) {
                out.close();
                out = new FileWriter(outFile + ".pub");
            }

            System.err.println("Public key format: " +
                    kp.getPublic().getFormat());
            out.write("-----BEGIN RSA PUBLIC KEY-----\n");
            out.write(encoder.encodeToString(pub.getEncoded()));
            out.write("\n-----END RSA PUBLIC KEY-----\n");
        } catch (IOException e) {
            log.info("密钥生成失败");
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.info("文件流关闭失败");
                    e.printStackTrace();
                }
            }
        }
    }

    public static RSAPrivateKey readRsaPvt() {
        return readRsaPvt(defaultLocation, defaultFilePrefix);
    }

    public static RSAPrivateKey readRsaPvt(String location, String filePrefix) {
        Path path = Paths.get(location + "/" + filePrefix + ".key");
        String data = "";
        try {
            data = new String(Files.readAllBytes(path));
        } catch (IOException e) {
            log.info("文件读取失败");
            e.printStackTrace();
        }
        data = data.replace("-----BEGIN RSA PRIVATE KEY-----\n", "")
                .replace("\n-----END RSA PRIVATE KEY-----\n", "");


        byte[] bytes = Base64.getDecoder().decode(data);

        PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
        KeyFactory kf = null;
        PrivateKey pvt = null;
        try {
            kf = KeyFactory.getInstance("RSA");
            pvt = kf.generatePrivate(ks);
        } catch (Exception e) {
            log.info("私钥读取失败");
            e.printStackTrace();
        }

        return (RSAPrivateKey) pvt;
    }

    public static RSAPublicKey readRsaPub() {
        return readRsaPub(defaultLocation, defaultFilePrefix);
    }

    public static RSAPublicKey readRsaPub(String location, String filePrefix) {
        Path path = Paths.get(location + "/" + filePrefix + ".pub");
        String data = "";
        try {
            data = new String(Files.readAllBytes(path));
        } catch (IOException e) {
            log.info("文件读取失败");
            e.printStackTrace();
        }
        data = data.replace("-----BEGIN RSA PUBLIC KEY-----\n", "")
                .replace("\n-----END RSA PUBLIC KEY-----\n", "");


        byte[] bytes = Base64.getDecoder().decode(data);

        X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
        KeyFactory kf = null;
        PublicKey pub = null;
        try {
            kf = KeyFactory.getInstance("RSA");
            pub = kf.generatePublic(ks);
        } catch (Exception e) {
            log.info("公钥读取失败");
            e.printStackTrace();
        }

        return (RSAPublicKey) pub;
    }

    public static void main(String[] args) {
        generateRSAKeys();
    }
}
