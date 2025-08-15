package org.hzai.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    /**
     * 加密
     */
    public static String encrypt(String input) {
        try {
            // 获取 MD5 摘要算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 执行摘要运算
            byte[] digest = md.digest(input.getBytes());
            // 转成 16 进制字符串
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 算法不可用", e);
        }
    }

    /**
     * 验证输入是否匹配MD5
     */
    public static boolean verify(String input, String md5Hash) {
        if (md5Hash.startsWith("{MD5}")) {
            md5Hash = md5Hash.replace("{MD5}", "");
        }
        return encrypt(input).equals(md5Hash);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            // & 0xff 处理负数
            String hex = Integer.toHexString(b & 0xff);
            if (hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
