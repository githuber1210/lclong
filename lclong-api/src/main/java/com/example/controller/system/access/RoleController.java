package com.example.controller.system.access;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.result.Result;
import com.example.models.entity.Role;
import com.example.framework.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@Api(tags = "角色模块")
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private IRoleService roleService;

    @ApiOperation(value = "添加角色")
    @PostMapping("/create")
    public Result create(@RequestBody Role role) {
        roleService.saveRole(role);
        return Result.success();
    }

    @ApiOperation(value = "修改角色信息")
    @PutMapping("/update")
    public Result update(@RequestBody Role role){
        roleService.updateRole(role);
        return Result.success();
    }

    @ApiOperation(value = "通过角色ID删除角色")
    @DeleteMapping("/{id}")
    public Result removeById(@PathVariable Integer id) {
        roleService.removeById(id);
        return Result.success();
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/list")
    public Result list(@RequestParam Integer pageNum, @RequestParam Integer pageSize,
                       @RequestParam(defaultValue = "") String keyword) {
        Page<Role> roleList = roleService.list(keyword,pageSize, pageNum);
        return Result.success(roleList);
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/listAll")
    public Result listAll() {
        List<Role> roleList = roleService.list();
        return Result.success(roleList);
    }
}

