package com.security.demo.send;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * Created by wuyabin on 2018/10/15.
 */
@Service
class SendMail {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${mail.fromMail.addr}")
    private String from;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private String port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    public boolean sendSimpleEmail(String to, String[] list, String subject, String content) {

        JavaMailSenderImpl senderMail = new JavaMailSenderImpl();
        // 设定 Mail Server
        senderMail.setHost(host);
        senderMail.setPort(Integer.valueOf(port));
        // SMTP验证时，需要用户名和密码
        senderMail.setUsername(username);
        senderMail.setPassword(password);

        Properties prop = new Properties();
        prop.setProperty("mail.smtp.starttls.enable", "true");
        prop.setProperty("mail.smtp.auth", "true");
        prop.setProperty("mail.smtp.connectiontimeout", "10000");
        prop.setProperty("mail.smtp.timeout", "10000");
        // 如果要密码验证,这里必须加,不然会报553错误
        senderMail.setJavaMailProperties(prop);


        SimpleMailMessage message;//创建简单邮件消息
        message = new SimpleMailMessage();
        //设置发送人
        message.setFrom(from);
        message.setTo(to);
        if (list != null) {
            message.setCc(list);
        }
        //设置主题
        message.setSubject(subject);
        //设置内容
        message.setText(content);
        try {
            //执行发送邮件
            senderMail.send(message);
            logger.info("sendMail-----end");
        } catch (Exception ex) {
            logger.error("发送简单邮件时发生异常！", ex);
            return false;
        }
        return true;
    }

}
