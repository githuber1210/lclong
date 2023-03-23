package com.example.framework.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.framework.bo.SecurityUserDetails;
import com.example.common.cache.RedisServiceImpl;
import com.example.common.constans.Constants;
import com.example.common.sms.SmsTemplateEnum;
import com.example.common.sms.VerifyCodeUntil;
import com.example.common.sms.tencentSmsUtil;
import com.example.framework.service.IMenuService;
import com.example.models.dto.Password;
import com.example.models.entity.Menu;
import com.example.models.entity.User;
import com.example.models.mapper.RoleMapper;
import com.example.models.mapper.RoleMenuMapper;
import com.example.models.mapper.UserMapper;
import com.example.framework.service.IUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private IMenuService menuService;
    @Resource
    private RedisServiceImpl redisService;

    @Override
    public User register(User user) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", user.getUsername());
        User DBuser= getOne(userQueryWrapper);
        if (DBuser == null) {
            user.setPassword(SecureUtil.md5(user.getPassword()));
            save(user);
            return user;
        } else {
//            throw new ServiceException(ResultCode.SERVICE_ERROR, "用户已存在");
            return null;
        }
    }

    @Override
    public void updatePassword(Password password) {
        password.setPassword(SecureUtil.md5(password.getPassword()));
        password.setNewPassword(SecureUtil.md5(password.getNewPassword()));
        if (userMapper.updatePassword(password) < 1) {
            //throw new ServiceException(ResultCode.SERVICE_ERROR, "原密码错误,修改密码失败!");
            return;
        }
    }

    @Override
    public User findByUsername(String username){
        return userMapper.findByUsername(username);
    }

    @Override
    public Page<User> list(String username, String telephone, Integer pageSize, Integer pageNum) {
        Page<User> page = new Page<>(pageNum,pageSize);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<User> lambda = wrapper.lambda();
        lambda.like(User::getUsername,username).like(User::getTelephone,telephone);
        return page(page,wrapper);
    }

    @Override
    public List<Menu> getMenusByRoleKey(String roleKey) {
        Long roleId = roleMapper.selectByFlag(roleKey);
        List<Long> mids = roleMenuMapper.selectMidsByRid(roleId);
        List<Menu> menus = menuService.listAllMenus();
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (mids.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // 移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child -> !mids.contains(child.getId()));
        }
        return roleMenus;
    }

    @Override
    public User getCurrentLoginUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        SecurityUserDetails securityUserDetails = (SecurityUserDetails)authentication.getPrincipal();
        User currentUser = findByUsername(securityUserDetails.getCurrentUsername());
        return currentUser;
    }

    @Override
    public boolean sendSMS(String teleNumber, SmsTemplateEnum smsTemplateEnum) {
        String flag = VerifyCodeUntil.getVerifyCodes(6);
        redisService.set(teleNumber + Constants.CODE_TELEPHONE, flag, 300);
        boolean result = tencentSmsUtil.sendMessage(teleNumber, smsTemplateEnum.getTemplateID(), flag, "5");
        return result;
    }

}
