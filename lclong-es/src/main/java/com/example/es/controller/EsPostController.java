package com.example.es.controller;

import com.example.es.common.result.Result;
import com.example.es.service.impl.EsPostServiceImpl;
import com.example.es.domain.EsPosts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(tags = "文章 ES")
@RequestMapping("/esPost")
public class EsPostController {
    @Autowired
    private EsPostServiceImpl esPostService;

    @ApiOperation(value = "导入所有文章到 ES")
    @RequestMapping(value = "/importAll", method = RequestMethod.POST)
    public Result importAll() {
        int count = esPostService.importAll();
        return Result.success(count);
    }

    @ApiOperation(value = "简单搜索")
    @RequestMapping(value = "/search/simple", method = RequestMethod.GET)
    public Result search(@RequestParam(required = false) String keyword,
                         @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                         @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        Page<EsPosts> page = esPostService.search(keyword,pageNum,pageSize);
        return Result.success(page);
    }
}
