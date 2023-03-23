package com.example.framework.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.framework.service.IMenuService;
import com.example.models.entity.Menu;
import com.example.models.entity.Role;
import com.example.models.entity.RoleMenu;
import com.example.models.entity.RolePermission;
import com.example.models.mapper.RoleMapper;
import com.example.models.mapper.RoleMenuMapper;
import com.example.models.mapper.RolePermissionMapper;
import com.example.framework.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private RolePermissionMapper rolePermissionMapper;

    @Resource
    private IMenuService menuService;

    @Resource
    private RoleMapper roleMapper;


    @Override
    public void saveRole(Role role) {
        role.setCreateTime(DateUtil.now());
        //role.setCreateBy(TokenUtils.getCurrentUser().getUsername());
        roleMapper.insert(role);
    }

    @Override
    public void updateRole(Role role) {
        role.setUpdateTime(DateUtil.now());
        //role.setUpdateBy(TokenUtils.getCurrentUser().getUsername());
        roleMapper.updateById(role);
    }

    @Override
    public List<Long> getRoleMenu(Long roleId) {
        return roleMenuMapper.selectMidsByRid(roleId);
    }

    @Transactional
    @Override
    public void setRoleMenu(Long roleId, List<Long> menuIds) {
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, roleId);
        roleMenuMapper.delete(queryWrapper);
        List<Long> menuIdsCopy = CollUtil.newArrayList(menuIds);
        for (Long menuId : menuIds) {
            Menu menu = menuService.getById(menuId);
            if (menu.getPid() != null && !menuIdsCopy.contains(menu.getPid())) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menu.getPid());
                roleMenuMapper.insert(roleMenu);
                menuIdsCopy.add(menu.getPid());
            }
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    public List<Long> getRolePermission(Long roleId) {
        return rolePermissionMapper.selectPidsByRid(roleId);

    }

    @Override
    public void setRolePermission(Long roleId, List<Long> pIds) {
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermission::getRoleId, roleId);
        rolePermissionMapper.delete(queryWrapper);
       // List<Integer> pIdsCopy = CollUtil.newArrayList(pIds);
        for (Long pid : pIds) {
//            Menu menu = menuService.getById(menuId);
//            if (menu.getPid() != null && !pIdsCopy.contains(menu.getPid())) {
//                RoleMenu roleMenu = new RoleMenu();
//                roleMenu.setRid(roleId);
//                roleMenu.setMid(menu.getPid());
//                roleMenuMapper.insert(roleMenu);
//                pIdsCopy.add(menu.getPid());
//            }
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(pid);
            rolePermissionMapper.insert(rolePermission);
        }
    }

    @Override
    public Page<Role> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<Role> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<Role> lambda = wrapper.lambda();
        lambda.like(Role::getName,keyword);
        return page(page,wrapper);
    }


}
