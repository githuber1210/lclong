package com.example.controller.system.common;


import com.example.common.result.Result;
import com.example.models.entity.Site;
import com.example.framework.service.ISiteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "站点")
@RestController
@RequestMapping("/site")
public class SiteController {
    @Resource
    private ISiteService siteService;

    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    @ApiOperation("获取站点配置")
    public Result getSite() {
        int count = (int) siteService.count();
        if (count == 0) {
            return Result.fail("没有配置信息");
        }
        List<Site> siteList = siteService.list();
        Site data = siteList.get(0);
        return Result.success(data);
    }
}

