package com.example.controller.system.monitor;


import com.example.common.constans.Constants;
import com.example.common.result.Result;
import com.example.models.domain.SysCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@Api(tags = "缓存模块")
@RestController
@RequestMapping("/cache")
public class CacheController {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private final static List<SysCache> caches = new ArrayList<SysCache>();
    {
        caches.add(new SysCache(Constants.LOGIN_USER_USERNAME_KEY, "用户信息"));
        caches.add(new SysCache(Constants.LOGIN_USER_PERMISSIONS_KEY, "用户信息"));
        caches.add(new SysCache(Constants.CAPTCHA_CODE_KEY, "邮箱验证码"));
    }

    @ApiOperation(value = "获取缓存信息")
    @GetMapping
    public Result getInfo() throws Exception
    {
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info());
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) connection -> connection.dbSize());

        Map<String, Object> result = new HashMap<>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StringUtils.removeStart(key, "cmdstat_"));
            data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);
        return Result.success(result);
    }

    @GetMapping("/getNames")
    public Result cache()
    {
        return Result.success(caches);
    }

    @GetMapping("/getKeys/{cacheName}")
    public Result getCacheKeys(@PathVariable String cacheName)
    {
        Set<String> cacheKeys = redisTemplate.keys(cacheName + "*");
        return Result.success(cacheKeys);
    }

    @GetMapping("/getValue/{cacheName}/{cacheKey}")
    public Result getCacheValue(@PathVariable String cacheName, @PathVariable String cacheKey)
    {
        String cacheValue = redisTemplate.opsForValue().get(cacheKey);
        SysCache sysCache = new SysCache(cacheName, cacheKey, cacheValue);
        return Result.success(sysCache);
    }

    @DeleteMapping("/clearCacheName/{cacheName}")
    public Result clearCacheName(@PathVariable String cacheName)
    {
        Collection<String> cacheKeys = redisTemplate.keys(cacheName + "*");
        redisTemplate.delete(cacheKeys);
        return Result.success();
    }

    @DeleteMapping("/clearCacheKey/{cacheKey}")
    public Result clearCacheKey(@PathVariable String cacheKey)
    {
        redisTemplate.delete(cacheKey);
        return Result.success();
    }

    @DeleteMapping("/clearCacheAll")
    public Result clearCacheAll()
    {
        Collection<String> cacheKeys = redisTemplate.keys("*");
        redisTemplate.delete(cacheKeys);
        return Result.success();
    }
}
