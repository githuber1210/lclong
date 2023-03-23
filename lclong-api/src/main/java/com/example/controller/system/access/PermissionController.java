package com.example.controller.system.access;


import com.example.common.result.Result;
import com.example.models.entity.Permission;
import com.example.framework.service.IPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "权限模块")
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Resource
    private IPermissionService permissionService;


    @ApiOperation(value = "添加权限")
    @PostMapping("/create")
    public Result create(@RequestBody Permission permission) {
        permissionService.save(permission);
        return Result.success();
    }

    @ApiOperation(value = "修改权限信息")
    @PutMapping("/update")
    public Result update(@RequestBody Permission permission) {
        permissionService.updateById(permission);
        return Result.success();
    }


    @ApiOperation(value = "列出所有权限")
    @GetMapping("/list")
    public Result list(){
        List<Permission> permissionList = permissionService.list();
        return Result.success(permissionList);
    }

    @ApiOperation(value = "删除权限")
    @DeleteMapping("/delete/{id}")
    public Result removeById(@PathVariable Integer id) {
        permissionService.removeById(id);
        return Result.success();
    }
}

