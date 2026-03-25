package com.running_platform.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface BaseRedisService<K, F, V> {
    void set(K key, V value);
    void expireKey(K key, long timeInSeconds, TimeUnit timeUnit);
    void setTimeToLive(K key, V value, long timeInSeconds, TimeUnit timeUnit);
    void hashSet(K key, F field, V value);
    void delete(K key);
    void hashDelete(K key, F field);
    void delete(K key, List<F> fields);
    Object hashGet(K key, F field);
    Object get(K key);
    Map<F, V> getField(K key);
    List<Object> hashGetByFieldPrefix(K key, F fieldPrefix);
    Set<F> getFieldPrefix(K key);
    boolean hashExist(K key, F field);
    boolean exists(K key);
    List<V> getListHash(K key);
    String buildKey(String prefix, Map<String, ?> params);
    String normalKey(String key, String... suffixes);

}