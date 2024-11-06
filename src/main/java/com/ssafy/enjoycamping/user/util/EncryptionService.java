package com.ssafy.enjoycamping.user.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.ssafy.enjoycamping.common.exception.BaseException;
import com.ssafy.enjoycamping.common.exception.InternalServerErrorException;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncryptionService {
    @Value("${security.AES256.secret-key}")
    private String secretKeyConfig;

    @Value("${security.AES256.iv}")
    private String ivConfig;

    private static SecretKey SECRET_KEY;
    private static IvParameterSpec IV;

    @PostConstruct
    public void init() {
        SECRET_KEY = new SecretKeySpec(Base64.getDecoder().decode(secretKeyConfig), "AES");
        IV = new IvParameterSpec(Base64.getDecoder().decode(ivConfig));
    }

    public static String encrypt(String data) throws BaseException {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY, IV);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new InternalServerErrorException(BaseResponseStatus.ENCRYPTION_ERROR);
        }
    }

    public static String decrypt(String encryptedData) throws BaseException {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY, IV);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new InternalServerErrorException(BaseResponseStatus.DECRYPTION_ERROR);
        }
    }
}