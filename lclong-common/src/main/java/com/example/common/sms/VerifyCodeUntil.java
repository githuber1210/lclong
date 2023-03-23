package com.example.common.sms;

import java.util.Random;

public class VerifyCodeUntil {
    private final static String sources = "5678012349"; // 验证码可能出现的字符
    public static String getVerifyCodes(int longSize){
        Random rand = new Random();
        StringBuffer flag = new StringBuffer();
        for (int j = 0; j < 6; j++) {
            flag.append(sources.charAt(rand.nextInt(9)) + "");
        }
        return String.valueOf(flag);
    }
}