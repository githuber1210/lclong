package com.example.mongodb.service.impl;

import com.example.mongodb.domain.UserBehavior;
import com.example.mongodb.repository.UserBehaviorRepository;
import com.example.mongodb.service.IUserBehaviorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserBehaviorServiceImpl implements IUserBehaviorService {

    @Resource
    private UserBehaviorRepository userBehaviorRepository;

    public void saveUserBehavior(String userId, String action) {
        UserBehavior userBehavior = new UserBehavior();
        userBehavior.setUserId(userId);
        userBehavior.setAction(action);
        userBehavior.setTimestamp(new Date());
        userBehaviorRepository.save(userBehavior);
    }

}