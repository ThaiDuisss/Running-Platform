package com.running_platform.service.redis;

import com.running_platform.service.BaseRedisService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BaseRedisServiceImpl<K, F, V> implements BaseRedisService<K, F, V> {
    RedisTemplate<K, V> redisTemplate;

    HashOperations<K, F, V> hashOperations;

    @Override
    public void set(K key, V value) {
        redisTemplate.opsForValue().set(key, value );
    }

    @Override
    public void expireKey(K key, long timeInSeconds, TimeUnit timeUnit) {
        redisTemplate.expire(key, timeInSeconds, timeUnit);
    }

    @Override
    public void setTimeToLive(K key, V value, long timeInSeconds, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeInSeconds, timeUnit);
    }

    @Override
    public void hashSet(K key, F field, V value) {
        hashOperations.put(key, field, value);
    }

    @Override
    public void delete(K key) {
        redisTemplate.delete(key);
    }

    @Override
    public void hashDelete(K key, F field) {
        hashOperations.delete(key, field);
    }

    @Override
    public void delete(K key, List<F> fields) {
        for(F field : fields) {
            hashOperations.delete(key, field);
        }
    }

    @Override
    public Object hashGet(K key, F field) {
        return hashOperations.get(key, field);
    }

    @Override
    public Object get(K key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Map<F, V> getField(K key) {
        return hashOperations.entries(key);
    }

    @Override
    public List<Object> hashGetByFieldPrefix(K key, F fieldPrefix) {
        return List.of();
    }

    @Override
    public Set<F> getFieldPrefix(K key) {
        return hashOperations.entries(key).keySet();
    }

    @Override
    public boolean hashExist(K key, F field) {
        return hashOperations.hasKey(key, field);
    }

    @Override
    public boolean exists(K key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public List<V> getListHash(K key) {
        if (key == null) return new ArrayList<>();

        Map<F, V> cart = getField(key);
        if (cart == null || cart.isEmpty()) return new ArrayList<>();

        return cart.values()
                .stream().map(v -> (V) v)
                .collect(Collectors.toList());
    }

    @Override
    public String buildKey(String prefix, Map<String, ?> params) {
        if(params == null || params.isEmpty()) {
            return prefix;
        }
        StringBuilder keyBuilder = new StringBuilder(prefix);

        Map<String, ?> sorted = new TreeMap<>(params);

        sorted.forEach((k, v) -> {
            keyBuilder.append(":")
                    .append(k)
                    .append("=")
                    .append(v != null ? v.toString().trim().toLowerCase() : "");
        });
        return keyBuilder.toString();
    }

    @Override
    public String normalKey(String key, String... suffixes) {
        if(suffixes == null || suffixes.length == 0) {
            return key;
        }
        StringBuilder keyBuilder = new StringBuilder(key);

        for(String suffix : suffixes) {
            if(StringUtils.hasText(suffix)) {
                keyBuilder.append(":").append(suffix.trim().toLowerCase());
            }
        }
        return keyBuilder.toString();
    }


}