package com.example.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MailServiceUtils{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private JavaMailSender mailSender;

    public void sendMail(String from,String to, String subject, String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        try {
            mailSender.send(message); logger.info("邮件成功发送!");
        } catch (MailException e) {
            logger.error("发送邮件错误:",e);
        }
    }
}