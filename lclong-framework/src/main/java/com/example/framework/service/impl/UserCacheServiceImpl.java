package com.example.framework.service.impl;

import com.example.common.cache.IRedisService;
import com.example.common.constans.Constants;
import com.example.models.entity.User;
import com.example.framework.service.IUserCacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserCacheServiceImpl implements IUserCacheService {

    @Value("${redis.expire.common}")
    private Long EXPIRE;

    @Resource
    private IRedisService redisService;

    @Override
    public User getUser(String username) {
        String key = Constants.LOGIN_USER_USERNAME_KEY + username;
        return (User) redisService.get(key);
    }

    @Override
    public void setUser(User user) {
        String key = Constants.LOGIN_USER_USERNAME_KEY + user.getUsername();
        redisService.set(key, user, EXPIRE);
    }

    @Override
    public void delUser(String username) {
        String key = Constants.LOGIN_USER_USERNAME_KEY + username;
        redisService.del(key);
    }

}