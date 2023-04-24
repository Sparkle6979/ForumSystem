package com.example.forumsystem;

import com.example.forumsystem.utils.MailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.junit.jupiter.api.Test;
import org.thymeleaf.context.Context;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/4/20 20:28
 */
@SpringBootTest
public class MailTests {

    @Autowired
    private MailClient mailClient;
    // 可以降低检测级别
    @Autowired(required = false)
    private TemplateEngine templateEngine;
    @Test
    public void testHtmlMail(){
        Context context=new Context();
        context.setVariable("username","sunday");
        String content=templateEngine.process("mail/demo",context);
        System.out.println(content);
        mailClient.sendMail("1249185069@qq.com","HTML",content);
    }
}
