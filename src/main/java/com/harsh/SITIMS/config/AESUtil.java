package com.harsh.SITIMS.config;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtil {

    private static final String AES = "AES";
    private static final String AES_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12; // 12 bytes recommended for GCM

    // Key loaded from environment variable AES_SECRET (must be exactly 16 chars)
    private static final String SECRET_KEY = System.getenv("AES_SECRET") != null
            ? System.getenv("AES_SECRET")
            : "defaultlocalkey!"; // fallback for local dev only

    public static String encrypt(String strToEncrypt) throws Exception {
        if (strToEncrypt == null) return null;

        // Generate a random IV for every encryption call
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);

        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), AES);
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);
        byte[] encrypted = cipher.doFinal(strToEncrypt.getBytes());

        // Prepend IV to ciphertext so decrypt can read it back
        byte[] ivPlusCipher = new byte[IV_LENGTH + encrypted.length];
        System.arraycopy(iv, 0, ivPlusCipher, 0, IV_LENGTH);
        System.arraycopy(encrypted, 0, ivPlusCipher, IV_LENGTH, encrypted.length);

        return Base64.getEncoder().encodeToString(ivPlusCipher);
    }

    public static String decrypt(String strToDecrypt) throws Exception {
        if (strToDecrypt == null) return null;

        byte[] ivPlusCipher = Base64.getDecoder().decode(strToDecrypt);

        // Extract IV from the first 12 bytes
        byte[] iv = new byte[IV_LENGTH];
        byte[] encrypted = new byte[ivPlusCipher.length - IV_LENGTH];
        System.arraycopy(ivPlusCipher, 0, iv, 0, IV_LENGTH);
        System.arraycopy(ivPlusCipher, IV_LENGTH, encrypted, 0, encrypted.length);

        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), AES);
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);
        byte[] decrypted = cipher.doFinal(encrypted);

        return new String(decrypted);
    }
}