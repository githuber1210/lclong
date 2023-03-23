package com.example.common.sms;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Desc 腾讯云短信模版枚举类型
 * @author Mintimate
 */
@Getter
@AllArgsConstructor
public enum SmsTemplateEnum {
    /**
     * 短信登录
     */
    LOGIN("1727459","{1}为您的验证码，请于{2}分钟内填写。如非本人操作，请忽略本短信"),

    ;
    private final String TemplateID;
    private final String TemplateDesc;
}