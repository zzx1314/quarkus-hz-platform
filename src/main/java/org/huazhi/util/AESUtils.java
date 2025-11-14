package org.huazhi.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AESUtils {
	 private static final String DEFAULT_KEY = "Welcome Superred";
    /**
     * AES CBC ZeroPadding 加密
     */
    public static String encrypt(String plainText, String keyStr) {
        try {
            String realKey = keyStr != null ? keyStr : DEFAULT_KEY;
            SecretKeySpec keySpec = new SecretKeySpec(realKey.getBytes(StandardCharsets.ISO_8859_1), "AES");
            IvParameterSpec iv = new IvParameterSpec(realKey.getBytes(StandardCharsets.ISO_8859_1));

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");

            // ZeroPadding: 需要我们手动补齐到 16 的倍数
            byte[] data = plainText.getBytes(StandardCharsets.UTF_8);
            int blockSize = 16;
            int paddedLength = ((data.length + blockSize - 1) / blockSize) * blockSize;
            byte[] paddedData = new byte[paddedLength];
            System.arraycopy(data, 0, paddedData, 0, data.length);

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            byte[] encrypted = cipher.doFinal(paddedData);

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("AES CBC ZeroPadding 加密失败", e);
        }
    }

    /**
     * AES CBC ZeroPadding 解密
     */
    public static String decrypt(String cipherText, String keyStr) {
        try {
            String realKey = keyStr != null ? keyStr : DEFAULT_KEY;
            SecretKeySpec keySpec = new SecretKeySpec(realKey.getBytes(StandardCharsets.ISO_8859_1), "AES");
            IvParameterSpec iv = new IvParameterSpec(realKey.getBytes(StandardCharsets.ISO_8859_1));

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

            byte[] decoded = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = cipher.doFinal(decoded);

            // 去掉 ZeroPadding 末尾的 \0
            int trimLength = decrypted.length;
            while (trimLength > 0 && decrypted[trimLength - 1] == 0) {
                trimLength--;
            }

            return new String(decrypted, 0, trimLength, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES CBC ZeroPadding 解密失败", e);
        }
    }
}
