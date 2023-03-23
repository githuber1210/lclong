package com.example.controller.system.common;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.constans.Constants;
import com.example.common.result.Result;
import com.example.framework.service.IDictService;
import com.example.models.entity.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "字典模块")
@RestController
@RequestMapping("/dict")
public class DictController {

    @Resource
    private IDictService dictService;

    @ApiOperation("分页查询")
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "") String keyword, @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        QueryWrapper<Dict> filesQueryWrapper = new QueryWrapper<>();
        filesQueryWrapper.like("name", keyword);
        Page<Dict> page = dictService.page(new Page<>(pageNum, pageSize), filesQueryWrapper);
        return Result.success(page);
    }

    @ApiOperation("添加字典")
    @PostMapping("/create")
    public Result save(@RequestBody Dict dict) {
        dictService.save(dict);
        return Result.success();
    }

    @ApiOperation("修改字典信息")
    @PutMapping("/update")
    public Result update(@RequestBody Dict dict){
        dict.setTime(DateUtil.now());
        dictService.updateById(dict);
        return Result.success();
    }

    @ApiOperation("删除字典")
    @DeleteMapping("/delete/{id}")
    public Result removeById(@PathVariable Integer id) {
        dictService.removeById(id);
        return Result.success();
    }

    @ApiOperation("获取字典中的性别集合")
    @GetMapping("/sex")
    public Result getSex() {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constants.DICT_TYPE_SEX);
        List<Dict> sexList = dictService.list(queryWrapper);
        return Result.success(sexList);
    }

    @ApiOperation("获取字典中的图标集合")
    @GetMapping("/icons")
    public Result getIcons() {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constants.DICT_TYPE_ICON);
        List<Dict> iconList = dictService.list(queryWrapper);
        return Result.success(iconList);
    }
}

