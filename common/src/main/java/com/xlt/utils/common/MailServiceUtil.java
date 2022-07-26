package com.xlt.utils.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Component
@ConditionalOnClass
public class MailServiceUtil {
    private static Logger logger = LoggerFactory.getLogger(MailServiceUtil.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:2219321592@qq.com}")
    private String from;

    /**
     * 发送简单邮件
     *
     * @param to      发送到
     * @param subject 主题
     * @param content 内容
     * @throws MailException 异常
     */
    public void sendSimpleMail(String to, String subject, String content) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); // 邮件发送者
        message.setTo(to); // 邮件接受者
        message.setSubject(subject); // 主题
        message.setText(content); // 内容
        mailSender.send(message);
        logger.info("send simple email successfully");
    }

    /**
     *发送带图片的邮件
     * @param to 发送到xxx
     * @param subject 主题
     * @param content 内容
     * @param rscPath 目标地址
     * @param rscId 目标ID
     * @throws MessagingException 异常
     */
    public void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        File file = new File(rscPath);
        FileSystemResource res = new FileSystemResource(file);
        helper.addInline(rscId, res);
        mailSender.send(message);
        logger.info("send email with picture successfully");
    }

    /**
     * 发送HTML的邮件
     * @param to 发送到
     * @param subject 主题
     * @param content 内容
     * @throws MessagingException 异常
     */
    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        //true 表⽰示需要创建⼀一个 multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
        logger.info("send email with html successfully");
    }

    /**
     * 发送带附件的邮件
     * @param to 发送到
     * @param subject 主题
     * @param content 内容
     * @param filePath 文件路径
     * @throws MessagingException 异常
     */
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        FileSystemResource file = new FileSystemResource(new File(filePath));
        String fileName = file.getFilename();
        helper.addAttachment(fileName, file);
        mailSender.send(message);
        logger.info("send email with attachment successfully");
    }
}
