package org.huazhi.util;

import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * IP工具类 - 完全 Quarkus 原生版本
 * @author justin.zheng
 * @date 2020/2/12
 */
@ApplicationScoped
public class IpUtils {

    private static final Logger logger = LoggerFactory.getLogger(IpUtils.class);

    /**
     * 获取用户真实IP地址
     * @param request Vert.x HttpServerRequest
     * @return 客户端IP
     */
    public String getIpAddr(HttpServerRequest request) {
        if (request == null) {
            return "unknown";
        }

        // X-Forwarded-For 代理头
        String ip = request.getHeader("X-Forwarded-For");
        logger.info("获得的ip地址是：" + ip);

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.remoteAddress() != null ? request.remoteAddress().host() : "unknown";
        }

        // 如果是多级代理，取第一个非 unknown
        if (ip != null && !ip.isEmpty()) {
            String[] split = ip.split(",");
            for (String s : split) {
                if (!"unknown".equalsIgnoreCase(s.trim())) {
                    ip = s.trim();
                    break;
                }
            }
        }

        return ip;
    }

    private IpUtils() {
        // 私有化构造
    }
}
