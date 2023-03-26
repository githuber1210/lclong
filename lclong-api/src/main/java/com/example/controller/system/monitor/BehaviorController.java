package com.example.controller.system.monitor;

import com.example.common.result.Result;
import com.example.common.log.IBehaviorService;
import com.example.models.domain.Behavior;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "用户行为记录")
@RestController
@RequestMapping("/behavior")
public class BehaviorController {
    @Resource
    private IBehaviorService behaviorService;

    @ApiOperation("新增")
    @PostMapping("/save")
    public Result save(String username,String action) {
        behaviorService.saveBehavior(username, action);
        return Result.success(null);
    }

    @ApiOperation("查看")
    @GetMapping("/list")
    public Result list(String username) {
        List<Behavior> list = behaviorService.list(username);
        return Result.success(list);
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public Result delete(@RequestParam("ids") String username) {
        int count = behaviorService.delete(username);
        if (count > 0) {
            return Result.success(count);
        } else {
            return Result.fail(null);
        }
    }

}