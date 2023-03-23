package com.example.common.sms;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "tencent")
public class TencentSmsConfigProperties {
    private String secretId;
    private String secretKey;
    private String sdkAppId;
    private String SignName;
}