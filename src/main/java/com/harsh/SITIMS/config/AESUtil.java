package com.harsh.SITIMS.config;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtil {

    private static final String AES = "AES";
    private static final String AES_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final byte[] IV = "1234567890123456".getBytes(); // 16 bytes
    private static final String SECRET_KEY = "mysupersecretkey"; // 16 chars

    public static String encrypt(String strToEncrypt) throws Exception {
        if (strToEncrypt == null) return null;
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), AES);
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, IV);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);
        byte[] encrypted = cipher.doFinal(strToEncrypt.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String strToDecrypt) throws Exception {
        if (strToDecrypt == null) return null;
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), AES);
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, IV);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);
        byte[] decoded = Base64.getDecoder().decode(strToDecrypt);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }
}
