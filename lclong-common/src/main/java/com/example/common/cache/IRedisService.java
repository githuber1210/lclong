package com.example.common.cache;

import java.util.List;
import java.util.Set;

public interface IRedisService {

    void set(String key, Object value, long time);

    void set(String key, Object value);

    Object get(String key);

    Boolean del(String key);

    Long del(List<String> keys);

    Boolean expire(String key, long time);

    public Set<String> getKeysByPattern(String pattern);

}