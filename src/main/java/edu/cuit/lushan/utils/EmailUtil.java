package edu.cuit.lushan.utils;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Random;


/**
 * 邮件发送工具类
 *
 * @author zhouhao
 */
@Component
public class EmailUtil {
    private static final String HOST = "smtp.163.com"; // 邮箱服务器
    private static final Integer PORT = 25;    // 端口
    private static final String USERNAME = "guoheng85@163.com";    //邮箱名
    private static final String PASSWORD = "VQQGOGPHWSTLDSJL";        //密码
    private static final String EMAILFORM = "guoheng85@163.com";  //发件人(需要与邮箱名一致，不然会出现553问题)
    private static JavaMailSenderImpl mailSender = createMailSender();
    private static LushanRedisUtil lushanRedisUtil = (LushanRedisUtil) ApplicationContextHelperUtil.getBean(LushanRedisUtil.class);

    /**
     * 邮件发送器
     *
     * @return 配置好的工具
     */
    private static JavaMailSenderImpl createMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(HOST);
        sender.setPort(PORT);
        sender.setUsername(USERNAME);
        sender.setPassword(PASSWORD);
        sender.setDefaultEncoding("Utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.timeout", "25000");
        p.setProperty("mail.smtp.auth", "false");
        sender.setJavaMailProperties(p);
        return sender;
    }


    /**
     * 发送邮件
     *
     * @param to      接受人
     * @param subject 主题
     * @param message 发送内容
     * @throws MessagingException           异常
     * @throws UnsupportedEncodingException 异常
     */
    public static void sendHtmlMail(String to, String subject, String message) throws MessagingException, UnsupportedEncodingException {
        String html = String.format(
                "<strong>%s</strong>" +
                        "<hr /><ul>" +
                        "<li>本邮件为自动发送, 请勿直接回复</li>" +
                        "<li>如非本人发起, 请确认账号是否已被他人盗用</li>" +
                        "<li>如有其他问题请发送邮件到 " +
                        "<a href=\"mailto:guoheng85@163.com\">" +
                        "guoheng85@163.com" +
                        "</a></li></ul><hr />\n", message);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(EMAILFORM, "");
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        mailSender.send(mimeMessage);
    }


    /**
     * 发送邮件
     *
     * @param to 接受人
     * @throws MessagingException           异常
     * @throws UnsupportedEncodingException 异常
     */
    public static void sendCodeMail(String to) throws MessagingException, UnsupportedEncodingException {
        String code = CodeUtil.createCodeString(6);
        lushanRedisUtil.saveForMinutes(to, code, 3L);
        String html = String.format(
                "您重置密码的验证码为：<strong>%s</strong>，30分钟内有效！" +
                        "<hr /><ul>" +
                        "<li>本邮件为自动发送, 请勿直接回复</li>" +
                        "<li>如非本人发起, 请确认账号是否已被他人盗用</li>" +
                        "<li>如有其他问题请发送邮件到 " +
                        "<a href=\"mailto:guoheng85@163.com\">" +
                        "guoheng85@163.com" +
                        "</a></li></ul><hr />\n", code);
        MimeMessage mimeMessage = mailSender.createMimeMessage();


        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(EMAILFORM, "");
        messageHelper.setTo(to);
        messageHelper.setSubject("重置密码");
        messageHelper.setText(html, true);
        mailSender.send(mimeMessage);
    }

    /**
     * 发送邮件
     *
     * @param to 接受人
     * @throws MessagingException           异常
     * @throws UnsupportedEncodingException 异常
     */
    public static void sendLinkMail(String to, String link) throws MessagingException, UnsupportedEncodingException {
        System.err.println("link= "+link);
        String html = String.format(
                "您的数据打包已完成<a href='%s'>点击即可下载</a>，12小时内有效！" +
                        "<hr /><ul>" +
                        "<li>本邮件为自动发送, 请勿直接回复</li>" +
                        "<li>如非本人发起, 请确认账号是否已被他人盗用</li>" +
                        "<li>如有其他问题请发送邮件到 " +
                        "<a href=\"mailto:guoheng85@163.com\">" +
                        "guoheng85@163.com" +
                        "</a></li></ul><hr />\n", link);
        System.err.println(html);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(EMAILFORM, "");
        messageHelper.setTo(to);
        messageHelper.setSubject("数据打包已成功！");
        messageHelper.setText(html, true);
        mailSender.send(mimeMessage);
    }
}