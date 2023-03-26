package com.example.controller.system.monitor;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.result.Result;
import com.example.models.entity.Log;
import com.example.common.log.ILogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
@Api(tags = "日志模块")
@RestController
@RequestMapping("/log")
public class LogController {
    @Resource
    private ILogService logService;

    @ApiOperation("分页查询")
    @GetMapping("/list")
    public Result page(@RequestParam(required = false, defaultValue = "") String username,
                       @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Log> query = Wrappers.<Log>lambdaQuery().orderByDesc(Log::getId);
        if (StrUtil.isNotBlank(username)) {
            query.like(Log::getUser, username);
        }
        Page<Log> page = logService.page(new Page<>(pageNum, pageSize), query);
        return Result.success(page);
    }
}
