package io.github.nnkwrik.authservice.token;

import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.*;
import java.util.Base64;

/**
 * 生成rsa的公钥和私钥
 * 需要把生成的公钥(.pub)拷贝到需要使用jwt的所有模块的resources目录下
 *
 * @author nnkwrik
 * @date 18/11/12 11:35
 */
@Slf4j
public class RSAKeysGenerator {

    public static final String defaultGenerateLocation = "auth-service/src/main/resources";

    public static final String defaultGenerateFilePrefix = "RSA";

    public static void generateRSAKeys() {
        generateRSAKeys(defaultGenerateLocation, defaultGenerateFilePrefix);
    }

    public static void generateRSAKeys(String location, String filePrefix) {
        //生成RSA对
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
            //输出RSA私钥
            if (outFile != null) out = new FileWriter(outFile + ".key");
            else out = new OutputStreamWriter(System.out);

            System.err.println("Private key format: " +
                    kp.getPrivate().getFormat());
            out.write("-----BEGIN RSA PRIVATE KEY-----\n");
            out.write(encoder.encodeToString(pvt.getEncoded()));
            out.write("\n-----END RSA PRIVATE KEY-----\n");

            //输出RSA公钥
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


    public static void main(String[] args) {
        generateRSAKeys();
    }
}
