package com.zrulin.ftcommunity;

import com.zrulin.ftcommunity.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.xml.ws.Action;

/**
 * @author zrulin
 * @create 2022-03-10 20:51
 */
@SpringBootTest
public class MailTest {
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Test
    public void test1(){
        mailClient.sendMail("linzru@qq.com","测试邮件","hello world!");
    }
    @Test
    public void test2(){
        Context context = new Context();
        context.setVariable("username","zrulin");
        String content = templateEngine.process("/mail/demo",context);
        System.out.println(content);
        mailClient.sendMail("linzru@qq.com","HtmlTest",content);
    }
}
