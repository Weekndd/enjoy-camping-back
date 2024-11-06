package com.ssafy.enjoycamping.common.util;
import io.jsonwebtoken.Jwts;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyGeneratorUtil {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // JWT 비밀 키 생성
        SecretKey JWTKey = Jwts.SIG.HS512.key().build();
        String base64Key = Base64.getEncoder().encodeToString(JWTKey.getEncoded());
        System.out.println("Generated Secret Key: " + base64Key);

        // AES 256 비밀 키 생성
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();
        String encodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Generated Secret Key (Base64): " + encodedSecretKey);

        // IV (16바이트) 생성
        byte[] ivBytes = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(ivBytes);
        String encodedIv = Base64.getEncoder().encodeToString(ivBytes);
        System.out.println("Generated IV (Base64): " + encodedIv);
    }
}
