package com.example.common.log.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.log.ILogService;
import com.example.models.entity.Log;
import com.example.models.mapper.LogMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

    @Resource
    private LogMapper logMapper;

    @Override
    public void log(String content,String username) {
        Log log = new Log();
        log.setUser(username);
        log.setTime(DateUtil.formatDateTime(new Date()));

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) attributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        log.setIp(ServletUtil.getClientIP(request));
        log.setContent(content);
        logMapper.insert(log);
    }



}
