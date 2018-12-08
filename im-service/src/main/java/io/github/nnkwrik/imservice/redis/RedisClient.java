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


    public <T> T hget(String key, String field) {
        return (T) redisTemplate.opsForHash().get(key, field);
    }

    public void hset(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    public <T> List<T> hvals(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    public void hdel(String key,String... fields){
        redisTemplate.opsForHash().delete(key,fields);
    }
}
