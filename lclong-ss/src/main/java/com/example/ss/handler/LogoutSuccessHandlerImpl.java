package com.example.ss.handler;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.example.common.cache.RedisServiceImpl;
import com.example.common.constans.Constants;
import com.example.common.log.IBehaviorService;
import com.example.common.log.ILogService;
import com.example.common.result.Result;
import com.example.common.util.ServletUtils;
import com.example.ss.util.JwtTokenUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler
{
    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private RedisServiceImpl redisService;

    @Resource
    private ILogService logService;

    @Resource
    private IBehaviorService behaviorService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {
        String username = jwtTokenUtil.getLoginUser(request);
        if (StrUtil.isNotEmpty(username))
        {
            redisService.del(Constants.LOGIN_USER_USERNAME_KEY + username);
            redisService.del(Constants.LOGIN_USER_PERMISSIONS_KEY + username);
            logService.log(Constants.LOGOUT,username);
            behaviorService.saveBehavior(username,Constants.LOGOUT);
        }
        ServletUtils.renderString(response, JSON.toJSONString(Result.success(null)));
    }

}
