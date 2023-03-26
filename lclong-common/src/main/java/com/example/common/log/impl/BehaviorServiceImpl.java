package com.example.common.log.impl;

import cn.hutool.core.date.DateUtil;
import com.example.common.log.IBehaviorService;
import com.example.models.domain.Behavior;
import com.example.models.repository.BehaviorRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BehaviorServiceImpl implements IBehaviorService {

    @Resource
    private BehaviorRepository behaviorRepository;

    @Override
    public int delete(String username){
        behaviorRepository.deleteAllByUsername(username);
        return 1;
    }

    @Override
    public List<Behavior> list(String username) {
        return behaviorRepository.findByUsername(username);
    }

    public void saveBehavior(String username, String action) {
        Behavior userBehavior = new Behavior();
        userBehavior.setUsername(username);
        userBehavior.setAction(action);
        userBehavior.setTime(DateUtil.now());
        behaviorRepository.save(userBehavior);
    }
}