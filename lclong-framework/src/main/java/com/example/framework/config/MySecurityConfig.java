package com.example.framework.config;


import com.example.models.entity.Permission;
import com.example.framework.service.ILoginService;
import com.example.framework.service.impl.PermissionServiceImpl;
import com.example.ss.service.DynamicSecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义配置，用于配置如何获取用户信息及动态权限
**/
@Configuration
public class MySecurityConfig {

    @Resource
    private ILoginService adminService;

    @Resource
    private PermissionServiceImpl permissionService;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> adminService.loadUserByUsername(username);
    }

    @Bean
    public DynamicSecurityService dynamicSecurityService() {
        return () -> {
            Map<String, ConfigAttribute> map = new ConcurrentHashMap<>();
                List<Permission> PermissionList = permissionService.list();
            for (Permission resource : PermissionList) {
                map.put(resource.getUrl(),
                // 将权限信息以"URL:权限配置信息"的形式添加到Map中
                new org.springframework.security.access.SecurityConfig(resource.getId() + ":" + resource.getName()));
            }
            return map;
        };
    }

}
