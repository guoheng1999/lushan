package edu.cuit.lushan.utils;

import edu.cuit.lushan.enums.EmailTypeEnum;
import edu.cuit.lushan.exception.MyRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.text.MessageFormat;
import java.util.Properties;


/**
 * 邮件发送工具类
 *
 * @author zhouhao
 */
@Component
@Slf4j
public class EmailUtil {
    private static final String HOST = "smtp.163.com"; // 邮箱服务器
    private static final Integer PORT = 25;    // 端口
    private static final String USERNAME = "guoheng85@163.com";    //邮箱名
    private static final String PASSWORD = "VQQGOGPHWSTLDSJL";        //密码
    private static final String EMAIL_FORM = "guoheng85@163.com";  //发件人(需要与邮箱名一致，不然会出现553问题)
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

    // 发送注册成功邮件
    public static void sendRegisterSuccessEmail(String to) throws MessagingException, UnsupportedEncodingException {
        String html = buildContent(EmailTypeEnum.REGISTER_SUCCESS);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(EMAIL_FORM, "");
        messageHelper.setTo(to);
        messageHelper.setSubject("庐山云雾观测数据集-审核通过");
        messageHelper.setText(html, true);
        mailSender.send(mimeMessage);
    }
    // 发送注册失败邮件
    public static void sendRegisterFailureEmail(String to, String reason) throws MessagingException, UnsupportedEncodingException {
        String html = buildContent(EmailTypeEnum.REGISTER_FAILURE, reason);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(EMAIL_FORM, "");
        messageHelper.setTo(to);
        messageHelper.setSubject("庐山云雾观测数据集-审核不通过");
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
    public static void sendChangePasswordEmail(String to) throws MessagingException, UnsupportedEncodingException {
        String code = CodeUtil.createCodeString(6);
        lushanRedisUtil.saveForMinutes(to, code, 3L);
        String html = buildContent(EmailTypeEnum.CHANGE_PASSWORD, code);
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(EMAIL_FORM, "");
        messageHelper.setTo(to);
        messageHelper.setSubject("庐山云雾观测数据集-重置密码");
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
    public static void sendDataPackageMail(String to, String  dataName, String fromDay, String endDay, String link) throws MessagingException, UnsupportedEncodingException {
        System.err.println("link= "+link);
        String html = buildContent(EmailTypeEnum.DATA_PACKAGE, dataName, fromDay, endDay, link);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(EMAIL_FORM, "");
        messageHelper.setTo(to);
        messageHelper.setSubject("庐山云雾观测数据集-您的数据准备已准备好");
        messageHelper.setText(html, true);
        mailSender.send(mimeMessage);
    }

    private static Resource getResourse(EmailTypeEnum emailTypeEnum) {
        //加载邮件html模板
        Resource resource;
        switch (emailTypeEnum) {
            case REGISTER_FAILURE:
                resource = new ClassPathResource("email-template-register-failure.ftl");
                break;
            case REGISTER_SUCCESS:
                resource = new ClassPathResource("email-template-register-success.ftl");
                break;
            case CHANGE_PASSWORD:
                resource = new ClassPathResource("email-template-code.ftl");
                break;
            case DATA_PACKAGE:
                resource = new ClassPathResource("email-template-data-link.ftl");
                break;
            default:
                throw new MyRuntimeException("邮件类型不存在");
        }
        return resource;
    }
    private static StringBuffer buildContentStringBuffer(Resource resource) {
        InputStream inputStream = null;
        BufferedReader fileReader = null;
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            inputStream = resource.getInputStream();
            fileReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = fileReader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            log.info("发送邮件读取模板失败{}", e.getMessage());
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer;
    }
    private static String dealRegisterSuccessBuffer(StringBuffer buffer) {
        return buffer.toString();
    }
    private static String dealRegisterFailureBuffer(StringBuffer buffer, String reason) {
        return MessageFormat.format(buffer.toString(), reason);
    }
    private static String dealChangePasswordBuffer(StringBuffer buffer, String code) {
        return MessageFormat.format(buffer.toString(), code);
    }
    private static String dealDataPackageBuffer(StringBuffer buffer, String dataName, String fromDay, String endDay, String link) {
        return MessageFormat.format(buffer.toString(), dataName,fromDay, endDay, link);
    }
    public static String buildContent(EmailTypeEnum emailTypeEnum, String... args) {
        Resource resource = getResourse(emailTypeEnum);
        StringBuffer buffer = buildContentStringBuffer(resource);
        switch (emailTypeEnum) {
            case REGISTER_FAILURE:
                return dealRegisterFailureBuffer(buffer, args[0]);
            case REGISTER_SUCCESS:
                return dealRegisterSuccessBuffer(buffer);
            case CHANGE_PASSWORD:
                return dealChangePasswordBuffer(buffer, args[0]);
            case DATA_PACKAGE:
                return dealDataPackageBuffer(buffer, args[0], args[1], args[2], args[3]);
            default:
                throw new MyRuntimeException("邮件类型不存在");
        }
    }
}