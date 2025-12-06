package org.huazhi.util;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.keys.KeyCommands;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Duration;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Redis 工具类
 */
@ApplicationScoped
public class RedisUtil {

    private final ValueCommands<String, String> valueCommands;
    private final KeyCommands<String> keyCommands;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    public RedisUtil(RedisDataSource ds) {
        this.valueCommands = ds.value(String.class);
        this.keyCommands = ds.key();
    }

    /**
     * 将对象存储到 Redis
     */
    public <T> void setObject(String key, T obj) {
        try {
            String json = objectMapper.writeValueAsString(obj);
            valueCommands.set(key, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void setObject(String key, T obj, long seconds) {
        try {
            String json = objectMapper.writeValueAsString(obj);
            valueCommands.setex(key, seconds, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从 Redis 获取对象
     */
    public <T> Optional<T> getObject(String key, Class<T> clazz) {
        String json = valueCommands.get(key);
        if (json == null)
            return Optional.empty();
        try {
            return Optional.of(objectMapper.readValue(json, clazz));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置 key-value
     */
    public void set(String key, String value) {
        valueCommands.set(key, value);
    }

    /**
     * 设置 key-value，并指定过期时间
     */
    public void set(String key, String value, long seconds) {
        valueCommands.setex(key, seconds, value);
    }

    /**
     * 获取 value
     */
    public String get(String key) {
        return valueCommands.get(key);
    }

    /**
     * 删除 key
     */
    public void del(String key) {
        keyCommands.del(key);
    }

    /**
     * 设置过期时间
     */
    public void expire(String key, long seconds) {
        keyCommands.expire(key, Duration.ofSeconds(seconds));
    }

    /**
     * 判断 key 是否存在
     */
    public boolean exists(String key) {
        return keyCommands.exists(key);
    }
}
