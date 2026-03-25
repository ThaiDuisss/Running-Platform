package com.running_platform.service.redis;


import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnClass(RedisTemplate.class)
public class RedisService extends BaseRedisServiceImpl<String, String, Object> {
    
    public RedisService(RedisTemplate<String, Object> redisTemplate, 
                       HashOperations<String, String, Object> hashOperations) {
        super(redisTemplate, hashOperations);
    }
}