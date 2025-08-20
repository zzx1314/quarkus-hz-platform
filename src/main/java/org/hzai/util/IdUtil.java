package org.hzai.util;

import java.util.UUID;

public class IdUtil {

    public static String simpleUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
