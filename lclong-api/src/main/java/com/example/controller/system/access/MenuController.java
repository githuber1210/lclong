package com.example.controller.system.access;


import com.example.common.result.Result;
import com.example.models.entity.Menu;
import com.example.framework.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Stream;

@Api(tags = "菜单模块")
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;


    @ApiOperation(value = "添加菜单")
    @PostMapping("/create")
    public Result create(@RequestBody Menu menu) {
        menuService.saveMenu(menu);
        return Result.success();
    }

    @ApiOperation(value = "修改菜单信息")
    @PutMapping("/update")
    public Result update(@RequestBody Menu menu) {
        menuService.updateMenu(menu);
        return Result.success();
    }

    @ApiOperation(value = "列出所有菜单ID")
    @GetMapping("/listIds")
    public Result listIds() {
        Stream<Long> ids = menuService.list().stream().map(Menu::getId);
        return Result.success(ids);
    }

    @ApiOperation(value = "列出所有菜单")
    @GetMapping("/list")
    public Result list(){
        List<Menu> menuList = menuService.listAllMenus();
        return Result.success(menuList);
    }

    @ApiOperation(value = "通过角色ID删除角色")
    @DeleteMapping("/delete/{id}")
    public Result removeById(@PathVariable Integer id) {
        menuService.removeById(id);
        return Result.success();
    }


}

