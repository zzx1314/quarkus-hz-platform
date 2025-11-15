package org.huazhi.drones.util;

import org.huazhi.util.RedisUtil;

import io.quarkus.runtime.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CheckClientTokenUtil {
    @Inject
    public RedisUtil redisUtil;


    /**
     * 检查token信息
     */
    public boolean checkToken(String clientId, String token) {
        String auth = (String)redisUtil.get(token);
        if (StringUtil.isNullOrEmpty(auth) || !clientId.equals(auth)){
            return false;
        } else {
            redisUtil.expire(token,  60 * 60 * 24);
        }
        return true;
    }
}
