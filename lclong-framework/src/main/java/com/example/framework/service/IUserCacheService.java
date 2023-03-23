package com.example.framework.service;

import com.example.models.entity.User;

public interface IUserCacheService {

    User getUser(String username);

    void setUser(User user);

    void delUser(String username);
}