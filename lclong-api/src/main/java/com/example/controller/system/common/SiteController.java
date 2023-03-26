package com.example.controller.system.common;


import com.example.common.result.Result;
import com.example.framework.service.ISiteService;
import com.example.models.entity.Site;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "站点")
@RestController
@RequestMapping("/site")
public class SiteController {
    @Resource
    private ISiteService siteService;

    @ApiOperation("获取站点配置")
    @GetMapping("/getById")
    public Result getSite() {
        List<Site> siteList = siteService.list();
        Site data = siteList.get(0);
        return Result.success(data);
    }

    @ApiOperation("修改站点配置")
    @PostMapping("/update")
    public Result update(@RequestBody Site site) {
        siteService.saveOrUpdate(site);
        return Result.success(null);
    }
}

