package com.example.framework.service;


import com.example.models.entity.Permission;
import com.example.models.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;


public interface ILoginService {
    String login(User user);

    Map<String, String> noPswLogin(User user);

    UserDetails loadUserByUsername(String username);

    User getUserByUsername(String username);

    List<Permission> getPermissionList(String username);

    void register(User user);


}
