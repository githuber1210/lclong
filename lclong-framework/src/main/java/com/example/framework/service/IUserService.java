package com.example.framework.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.sms.SmsTemplateEnum;
import com.example.models.dto.Password;
import com.example.models.entity.Menu;
import com.example.models.entity.User;

import java.util.List;

public interface IUserService extends IService<User> {


    User register(User user);

    void updatePassword(Password password);

    User findByUsername(String username);

    Page<User> list(String username, String password, Integer pageSize, Integer pageNum);

    List<Menu> getMenusByRoleKey(String roleKey);

    User getCurrentLoginUser();

    boolean sendSMS(String teleNumber, SmsTemplateEnum smsTemplateEnum);
}
