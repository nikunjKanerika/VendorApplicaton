package com.kanerika.Vendor.aes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.kanerika.Vendor.aes.AesEncryptor;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class AesEncryptor {

    private static final Logger logger = LoggerFactory.getLogger(AesEncryptor.class.getSimpleName());

    private static byte[] aesKeyBytes;
    private static Cipher decryptionCipher = null;
    private static Cipher encryptionCipher = null;

    public static void init() {
        if (decryptionCipher == null && encryptionCipher == null) {
            aesKeyBytes = loadKey("run/aes_key.bin");
            decryptionCipher = initDecryptionCipher(aesKeyBytes);
            encryptionCipher = initEncryptionCipher(aesKeyBytes);
        }
    }

    private static byte[] loadKey(String keyFilePath) {
        try {
            return Files.readAllBytes(Paths.get(keyFilePath));
        } catch (Exception e) {
            logger.error(String.format("Error loading AES key from file %s", keyFilePath));
            return new byte[0];
        }
    }

    private static Cipher initEncryptionCipher(byte[] aesKeyBytes) {
        Cipher encryptionCipher = null;
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(aesKeyBytes, "AES");
            encryptionCipher = Cipher.getInstance("AES");
            encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        } catch (Exception e) {
            logger.error(String.format("Error initializing AES decryption cipher: %s", e.getMessage()));
        }
        return encryptionCipher;
    }

    private static Cipher initDecryptionCipher(byte[] aesKeyBytes) {
        Cipher decryptionCipher = null;
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(aesKeyBytes, "AES");
            decryptionCipher = Cipher.getInstance("AES");
            decryptionCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        } catch (Exception e) {
            logger.error(String.format("Error initializing AES decryption cipher: %s", e.getMessage()));
        }
        return decryptionCipher;
    }

    public static String passwordEncryptor(String password) {
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedPasswordBytes;
        try {
            encryptedPasswordBytes = encryptionCipher.doFinal(passwordBytes);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return Base64.getEncoder().encodeToString(encryptedPasswordBytes);
    }

    public static String passwordDecryptor(String encryptedPassword) {
        byte[] encryptedPasswordBytes = Base64.getDecoder().decode(encryptedPassword);
        byte[] decryptedPasswordBytes;
        try {
            decryptedPasswordBytes = decryptionCipher.doFinal(encryptedPasswordBytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return new String(decryptedPasswordBytes, StandardCharsets.UTF_8);
    }
    public static String usernameEncryptor(String username) {
        byte[] usernameBytes = username.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedUsernameBytes;
        try {
            encryptedUsernameBytes = encryptionCipher.doFinal(usernameBytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return Base64.getEncoder().encodeToString(encryptedUsernameBytes);
    }

    public static String usernameDecryptor(String encryptedUsername) {
        byte[] encyptedUsernameBytes = Base64.getDecoder().decode(encryptedUsername);
        byte[] decryptedUsernameBytes;
        try {
            decryptedUsernameBytes = decryptionCipher.doFinal(encyptedUsernameBytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return new String(decryptedUsernameBytes, StandardCharsets.UTF_8);
    }
}
