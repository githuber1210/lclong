package com.example.controller.system.access;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.cache.RedisServiceImpl;
import com.example.common.constans.Constants;
import com.example.common.exception.ServiceException;
import com.example.common.log.ILogService;
import com.example.common.result.Result;
import com.example.common.result.ResultCode;
import com.example.common.sms.SmsTemplateEnum;
import com.example.common.sms.VerifyCodeUntil;
import com.example.common.util.MailServiceUtils;
import com.example.models.entity.User;
import com.example.framework.service.ILoginService;
import com.example.framework.service.IUserCacheService;
import com.example.framework.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.*;


@Api(tags = "登录模块")
@RestController
@RequestMapping
public class LoginController {

    @Resource
    private ILoginService loginService;
    @Resource
    private ILogService logService;
    @Resource
    private RedisServiceImpl redisService;
    @Resource
    private IUserCacheService userCacheService;
    @Resource
    private UserServiceImpl userService;
    @Resource
    private MailServiceUtils mailServiceUtils;

    @ApiOperation(value = "登录")
    @PostMapping(value = "/login")
    public Result login(@RequestBody User user) {
        String token = null ;
        try {
            token = loginService.login(user);
        }catch (Exception e){
            throw new ServiceException(ResultCode.SERVICE_ERROR,"用户名或密码错误");
        }
        logService.log(Constants.LOGIN,user.getUsername());
        HashMap<String, String> data = new HashMap<>();
        data.put("token",token);
        return Result.success(data);
    }

    @ApiOperation("手机登录")
    @GetMapping("/VerifyCodesByTelephone")
    public Result VerifyCodesByTelephone(@RequestParam(value = "telephone") String telephone,
                                         @RequestParam(value = "code") String code) {
        Object redisCodes = redisService.get(telephone + Constants.CODE_TELEPHONE);
        if (redisCodes == null) {
            return Result.fail("该手机号并未发送验证码");
        }
        if (redisCodes.equals(code)) {
            redisService.del(telephone + Constants.CODE_TELEPHONE);
            User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getTelephone,telephone));
            loginService.noPswLogin(user);
            Map<String,String> data = loginService.noPswLogin(user);
            logService.log(Constants.LOGIN , user.getUsername());

            return Result.success(data);
        } else {
            return Result.fail("验证码错误");
        }
    }

    @ApiOperation("邮箱登录")
    @GetMapping("/VerifyCodesByEmail")
    public Result VerifyCodesByEmail(@RequestParam(value = "email") String email,
                                     @RequestParam(value = "code") String code) {
        String redisCaptcha = null;
        try {
            if (redisService.get(email) != null) {
                redisCaptcha = (String) redisService.get(email);
            }
            if (redisCaptcha.equals(code)) {
                redisService.del(email);
                User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getEmail,email));
                Map<String,String> data = loginService.noPswLogin(user);
                return Result.success(data);
            }
        } catch (Exception e) {
            throw new ServiceException(ResultCode.SERVICE_ERROR, "验证码错误");
        }
        return Result.fail("验证码错误");
    }

    @ApiOperation("获得手机验证码")
    @GetMapping("/getVerifyCodesByTelephone")
    public Result getVerifyCodesByTelephone(@RequestParam String telephone) {
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getTelephone,telephone));
        if (Objects.isNull(user)) {
            return Result.fail("该手机号未绑定用户");
        }
        userService.sendSMS(telephone, SmsTemplateEnum.LOGIN);
        return Result.success("发送成功");
    }

    @ApiOperation("发送邮箱验证码")
    @GetMapping("/getVerifyCodesByEmail")
    public Result getVerifyCodesByEmail(@RequestParam String email) {
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getEmail,email));
        if (Objects.isNull(user)) {
            return Result.fail("该邮箱未绑定用户");
        }
        try {
            String Captcha = VerifyCodeUntil.getVerifyCodes(6);
            mailServiceUtils.sendMail("3353115002@qq.com", email, "验证码", Captcha);
            redisService.set(email, Captcha,300);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.SERVICE_ERROR,"发送失败");
        }
        return Result.success("发送成功");
    }




    @ApiOperation(value = "获取所有在线用户")
    @GetMapping("/online")
    public Result list()
    {
        Collection<String> keys = redisService.getKeysByPattern(Constants.LOGIN_USER_USERNAME + "*");
        List<User> userOnlineList = new ArrayList<>();
        for (String key : keys)
        {
            User user = (User) redisService.get(key);
            userOnlineList.add(user);
        }
        Collections.reverse(userOnlineList);
        userOnlineList.removeAll(Collections.singleton(null));
        return Result.success(userOnlineList);
    }

    @ApiOperation(value = "强制下线")
    @DeleteMapping("/break/{username}")
    public Result forceBreak(@PathVariable String username)
    {
        userCacheService.delUser(username);
        return Result.success();
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping(value = "/userInfo")
    public Result getUserInfo(Principal principal) {
        if(principal == null){
            throw new ServiceException(ResultCode.SERVICE_ERROR,"身份验证失败");
        }
        String username = principal.getName();
        User user = loginService.getUserByUsername(username);
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("menus", userService.getMenusByRoleKey(user.getRole()));
        return Result.success(data);
    }

    @ApiOperation(value = "注册")
    @PostMapping(value = "/register")
    public Result register(@RequestBody User user) {
        loginService.register(user);
        return Result.success();
    }
}
