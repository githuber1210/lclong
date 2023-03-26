package com.example.mongodb.controller;

import com.example.mongodb.common.result.Result;
import com.example.mongodb.domain.History;
import com.example.mongodb.service.IHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "浏览记录")
@RestController
@RequestMapping("/history")
public class HistoryController {
    @Resource
    private IHistoryService HistoryService;

    @ApiOperation("查看")
    @GetMapping("/list")
    public Result list(Long memberId) {
        List<History> HistoryList = HistoryService.list(memberId);
        return Result.success(HistoryList);
    }

    @ApiOperation("新增")
    @PostMapping("/create")
    public Result create(@RequestBody History History) {
        int count = HistoryService.create(History);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.fail(null);
        }
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public Result delete(@RequestParam("ids") List<String> ids) {
        int count = HistoryService.delete(ids);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.fail(null);
        }
    }

}