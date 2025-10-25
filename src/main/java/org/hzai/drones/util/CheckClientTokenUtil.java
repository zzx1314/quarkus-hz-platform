package org.hzai.drones.util;

import org.hzai.util.RedisUtil;

import io.quarkus.runtime.util.StringUtil;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CheckClientTokenUtil {
    @Resource
    public RedisUtil redisUtil;


    /**
     * 检查token信息
     */
    public boolean checkToken(String clientId, String token) {
        String auth = (String)redisUtil.get(token);
        if (StringUtil.isNullOrEmpty(auth) || !clientId.equals(auth)){
            return false;
        } else {
            redisUtil.expire(token, 60 * 10);
        }
        return true;
    }
}
