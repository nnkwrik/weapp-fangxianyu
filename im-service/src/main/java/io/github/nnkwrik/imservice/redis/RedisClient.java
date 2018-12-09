package io.github.nnkwrik.imservice.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author nnkwrik
 * @date 18/12/06 15:08
 */
@Component
public class RedisClient {
    @Autowired
    private RedisTemplate redisTemplate;


    public <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    public <T> List<T> multiGet(List<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }


}
