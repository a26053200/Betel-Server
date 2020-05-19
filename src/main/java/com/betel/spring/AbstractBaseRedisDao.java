package com.betel.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @Description
 * @Author zhengnan
 * @Date 2020/5/19
 */
public class AbstractBaseRedisDao<K,V>
{
    @Autowired
    protected RedisTemplate<K, V> redisTemplate;

    public RedisTemplate<K, V> getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * 获取 RedisSerializer
     */
    protected RedisSerializer<String> getRedisSerializer() {

        return redisTemplate.getStringSerializer();
    }
}
