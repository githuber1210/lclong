package com.example.framework.service;

/**
 * description: 短信业务
 *
 * @author RenShiWei
 **/
public interface ISmsService {

    /**
     * description: 发送短信验证码
     *
     * @param phone 手机号
     */
    void sendSmsCode(String phone);

    /**
     * description:验证短信验证码
     *
     * @param phone 手机号
     * @param code  验证码
     */
    void verifyCode(String phone, String code);

}
