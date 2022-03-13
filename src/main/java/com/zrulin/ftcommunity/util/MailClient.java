package com.zrulin.ftcommunity.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


/**
 * 发送邮件的util
 * @author zrulin
 * @create 2022-03-10 20:36
 */
@Component
public class MailClient {
    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    @Autowired
    private JavaMailSender mailSender;

    //设置发送方
    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to , String subject, String content){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from); //设置发送方
            helper.setTo(to);  //设置接收方
            helper.setSubject(subject); //设置邮件标题
            helper.setText(content,true);  //设置邮件内容，true表明允许支持html文本
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("发送邮件失败："+e.getMessage());
        }
    }
}
