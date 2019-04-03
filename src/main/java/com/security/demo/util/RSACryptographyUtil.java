package com.security.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA工具类
 *
 * @author fanglingxiao
 */
public class RSACryptographyUtil {

    private static Logger logger = LoggerFactory.getLogger(RSACryptographyUtil.class);

    /**
     * 将base64编码后的公钥字符串转成PublicKey实例.
     *
     * @author fanglingxiao
     * @date 2019-03-18
     * @param publicKey publicKey
     * @return PublicKey
     */
    public static PublicKey getPublicKey(String publicKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(publicKey.getBytes());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            logger.error("获取PublicKey实例失败:", e);
        }
        return null;
    }

    /**
     * 将base64编码后的私钥字符串转成PrivateKey实例.
     *
     * @author fanglingxiao
     * @date 2019-03-18
     * @param privateKey privateKey
     * @return PublicKey
     */
    public static PrivateKey getPrivateKey(String privateKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(privateKey.getBytes());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            logger.error("获取PrivateKey实例:", e);
        }
        return null;
    }
}