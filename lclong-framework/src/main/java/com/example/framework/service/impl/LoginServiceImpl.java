package com.example.framework.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.framework.bo.SecurityUserDetails;
import com.example.common.constans.Constants;
import com.example.common.exception.ServiceException;
import com.example.common.result.ResultCode;
import com.example.common.util.ip.AddressUtils;
import com.example.models.entity.Menu;
import com.example.models.entity.Permission;
import com.example.models.entity.User;
import com.example.models.mapper.PermissionMapper;
import com.example.framework.service.ILoginService;
import com.example.framework.service.IUserCacheService;
import com.example.framework.service.IUserService;
import com.example.ss.util.JwtTokenUtil;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class LoginServiceImpl implements ILoginService {

    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private IUserCacheService userCacheService;
    @Resource
    private IUserService userService;
    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public String login(User user) {
        String token = null;
        try {

            UserDetails userDetails = loadUserByUsername(user.getUsername());
            if(userDetails == null ){
                throw new ServiceException(ResultCode.SERVICE_ERROR,"ServiceException");
            }

            if (!passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
                throw new BadCredentialsException("BadCredentialsException");
            }

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            token = Constants.TOKEN_PREFIX + jwtTokenUtil.generateToken(userDetails);

        } catch (AuthenticationException e) {
            log.warn("AuthenticationException:{}", e.getMessage());
        }
        return token;
    }

    @Override
    public Map<String, String> noPswLogin(User user) {
        Map<String, String> data = null;
        try {
            UserDetails userDetails = loadUserByUsername(user.getUsername());
            if(userDetails == null ){
                throw new ServiceException(ResultCode.SERVICE_ERROR,"ServiceException");
            }
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            String token = jwtTokenUtil.generateToken(userDetails);
            data = new HashMap<>();
            data.put("token", Constants.TOKEN_PREFIX + token);
        } catch (AuthenticationException e) {
            log.warn("AuthenticationException:{}", e.getMessage());
        }
        return data;
    }

    @Override
    public void register(User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User dbUser = getUserInfo(newUser);
        if (dbUser == null) {
            dbUser = new User();
            BeanUtil.copyProperties(newUser, dbUser, true);

            dbUser.setRole(Constants.ROLE_USER);
            if (dbUser.getUsername() == null) {
                dbUser.setUsername(dbUser.getUsername());
            }
            userService.save(dbUser);
        } else {
            throw new ServiceException(ResultCode.SERVICE_ERROR, "用户已存在");
        }
    }

    private User getUserInfo(User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        User dbUser = userService.getOne(queryWrapper);
        return dbUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        List<Permission> permissionList = getPermissionList(username);
        User loginUser = getUserByUsername(username);
        loginUser.setPermissionList(permissionList);
        userCacheService.setUser(loginUser);
        String password = loginUser.getPassword();
        SecurityUserDetails userDetails = new SecurityUserDetails(username,password,permissionList);
        return userDetails;
    }

    @Override
    public User getUserByUsername(String username) {
        User loginUser = userCacheService.getUser(username);
        if (loginUser != null){
            return loginUser;
        }
        loginUser = userService.findByUsername(username);

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) attributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        String header = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(header);

        String ip = ServletUtil.getClientIP(request);
        loginUser.setIpaddr(ip);

        String loginLocation = AddressUtils.getRealAddressByIP(ip);
        loginUser.setLoginLocation(loginLocation);

        String browserName = userAgent.getBrowser().getName();
        loginUser.setBrowser(browserName);

        String OsName = userAgent.getOperatingSystem().getName();
        loginUser.setOs(OsName);

        List<Menu> menus = userService.getMenusByRoleKey(loginUser.getRole());
        loginUser.setMenus(menus);

        String loginTime = DateUtil.formatDateTime(new Date());
        loginUser.setLoginTime(loginTime);

        userService.saveOrUpdate(loginUser);

        return loginUser;
    }



    @Override
    public List<Permission> getPermissionList(String username) {

        User user = getUserByUsername(username);
        List<Permission> permissionList = null;

        User userCache = userCacheService.getUser(username);
        if(userCache != null){
            permissionList = userCache.getPermissionList();
        }

        if(CollUtil.isNotEmpty(permissionList)){
            return  permissionList;
        }

        permissionList = permissionMapper.getPermissionList(user.getId());
        return permissionList;
    }

}
