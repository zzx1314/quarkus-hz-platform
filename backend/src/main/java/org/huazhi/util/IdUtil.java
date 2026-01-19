package org.huazhi.util;

import java.util.UUID;

import io.netty.util.internal.ThreadLocalRandom;

public class IdUtil {

    public static String simpleUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String randomCode(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

}
