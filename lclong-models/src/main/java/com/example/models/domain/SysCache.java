package com.example.models.domain;


import cn.hutool.core.util.StrUtil;
import lombok.Data;


@Data
public class SysCache
{
    private String cacheName = "";
    private String cacheKey = "";
    private String cacheValue = "";
    private String remark = "";


    public SysCache(String cacheName, String remark)
    {
        this.cacheName = cacheName;
        this.remark = remark;
    }

    public SysCache(String cacheName, String cacheKey, String cacheValue)
    {
        this.cacheName = StrUtil.replace(cacheName, ":", "");
        this.cacheKey = StrUtil.replace(cacheKey, cacheName, "");
        this.cacheValue = cacheValue;
    }

}
