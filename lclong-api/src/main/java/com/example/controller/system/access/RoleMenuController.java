package com.example.controller.system.access;

import com.example.common.result.Result;
import com.example.framework.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
@Api(tags = "角色-菜单模块")
@RestController
@RequestMapping("/roleMenu")
public class RoleMenuController {

    @Resource
    private IRoleService roleService;

    @ApiOperation(value = "通过角色ID获取所绑定的菜单")
    @GetMapping("/{roleId}")
    public Result getRoleMenu(@PathVariable Long roleId) {
        List<Long> roleMenu = roleService.getRoleMenu(roleId);
        return Result.success(roleMenu);
    }

    @ApiOperation(value = "修改角色绑定的菜单ID集合")
    @PostMapping("/{roleId}")
    public Result setRoleMenu(@PathVariable Long roleId, @RequestBody List<Long> menuIds) {
        roleService.setRoleMenu(roleId, menuIds);
        return Result.success();
    }


    @ApiOperation(value = "通过角色ID获取所绑定的权限")
    @GetMapping("/permission/{roleId}")
    public Result getRolePermission(@PathVariable Long roleId) {
        List<Long> rolePermission = roleService.getRolePermission(roleId);
        return Result.success(rolePermission);
    }

    @ApiOperation(value = "修改角色绑定的权限ID集合")
    @PostMapping("/permission/{roleId}")
    public Result setRolePermission(@PathVariable Long roleId, @RequestBody List<Long> menuIds) {
        roleService.setRolePermission(roleId, menuIds);
        return Result.success();
    }

}
