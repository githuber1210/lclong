package com.example.framework.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.models.entity.Role;

import java.util.List;


public interface IRoleService extends IService<Role> {

    Page<Role> list(String keyword, Integer pageSize, Integer pageNum);

    void saveRole(Role role);

    void updateRole(Role role);

    List<Long> getRoleMenu(Long roleId);

    void setRoleMenu(Long roleId, List<Long> menuIds);

    List<Long> getRolePermission(Long roleId);

    void setRolePermission(Long roleId, List<Long> pIds);



}
