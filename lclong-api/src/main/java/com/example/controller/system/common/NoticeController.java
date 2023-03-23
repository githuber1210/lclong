package com.example.controller.system.common;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.result.Result;
import com.example.framework.service.INoticeService;
import com.example.models.entity.Notice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "通告模块")
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private INoticeService noticeService;

    @ApiOperation(value = "添加通告")
    @PostMapping("/create")
    public Result save(@RequestBody Notice notice) {
        notice.setCreateTime(DateUtil.now());
        //notice.setAuthor(TokenUtils.getCurrentUser().getUsername());
        noticeService.save(notice);
        return Result.success();
    }

    @ApiOperation(value = "修改通告信息")
    @PutMapping("/update")
    public Result update(@RequestBody Notice notice){
        noticeService.updateById(notice);
        return Result.success();
    }

    @ApiOperation(value = "根据通告ID删除通告")
    @PostMapping("/delete/batch")
    public Result removeByIds(@RequestBody List<Integer> ids) {
        noticeService.removeByIds(ids);
        return Result.success();
    }

    @ApiOperation(value = "根据时间区间查询通告")
    @GetMapping("/retrieve")
    public Result findByTimeRange(@RequestParam(required = false) String start, @RequestParam(required = false) String end) {
        List<Notice> noticeList = noticeService.list(start,end);
        return Result.success(noticeList);
    }

    @ApiOperation(value = "根据通告ID获取通告信息")
    @GetMapping("/retrieve/{id}")
    public Result getById(@PathVariable Integer id) {
        Notice notice= noticeService.getById(id);
        return Result.success(notice);
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/list")
    public Result list(@RequestParam String title,
                       @RequestParam Integer pageNum,
                       @RequestParam Integer pageSize) {
        Page<Notice> noticePage = noticeService.list(title, pageNum, pageSize);
        return Result.success(noticePage);
    }

}

