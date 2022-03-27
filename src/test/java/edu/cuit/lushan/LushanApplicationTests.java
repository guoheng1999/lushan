package edu.cuit.lushan;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import edu.cuit.lushan.service.ICurrentDataService;
import edu.cuit.lushan.thread.DownLoadFileThread;
import edu.cuit.lushan.utils.EmailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.io.File;
import java.io.UnsupportedEncodingException;

//import edu.cuit.lushan.utils.LushanRedisUtil;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;


@SpringBootTest
class LushanApplicationTests {
    //
//    @Autowired
//    StringRedisTemplate stringRedisTemplate;
//    @Autowired
//    LushanRedisUtil<Device> lushanRedisUtil;
    @Test
    void contextLoads() {
    }

    @Autowired
    ICurrentDataService currentDataService;

    //    @Test
//    void testHutool(){
//        UserInfoVO releaseVersion = new UserInfoVO();
//        releaseVersion.setEmail("sss");
//        User old = new User();
//        old.setId(1);
//        old.setPassword("phone");
//        copyIgnoreNullValue(releaseVersion, old);
//        System.out.println(JSONUtil.toJsonStr(old));
//    }
//
//    private void copyIgnoreNullValue(UserInfoVO releaseVersion, User old) {
//        BeanUtil.copyProperties(releaseVersion,old, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
//
//
//    }
//    @Test
//    void testRedis(){
//        Device device = Device.builder()
//                .deviceName("雨量筒")
//                .description("这是雨量筒")
//                .modifyUserId(Integer.valueOf(123))
//                .id(1)
//                .isDelete(0)
//                .modifyTime(LocalDateTimeUtil.now())
//                .build();
//        String jsonStr = JSON.toJSONString(device);
//        stringRedisTemplate.opsForValue().set("test-key", jsonStr);
//        String getStr = stringRedisTemplate.opsForValue().get("test-key");
//        Device getDevice = JSON.parseObject(getStr, Device.class);
//        System.out.println("getDevice = " + getDevice);
//    }
//    @Test
//    void testLushanRedis(){
//        Device device = Device.builder()
//                .deviceName("雨量筒")
//                .description("这是雨量筒")
//                .modifyUserId(Integer.valueOf(123))
//                .id(1)
//                .isDelete(0)
//                .modifyTime(LocalDateTimeUtil.now())
//                .build();
//
//        Device device2 = Device.builder()
//                .deviceName("雨量筒")
//                .description("这是一个雨量筒")
//                .modifyUserId(Integer.valueOf(123))
//                .id(1)
//                .isDelete(0)
//                .build();
//        lushanRedisUtil.save("yuliangtong", device);
//        System.out.println(lushanRedisUtil.get("yuliangtong", Device.class));
//        lushanRedisUtil.save("yuliangtong", device2);
//        System.out.println(lushanRedisUtil.get("yuliangtong", Device.class));
//    }
//    @Test
//    void testAes(){
//        Device device = lushanRedisUtil.get("yuliangtong", Device.class);
//        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
//        AES aes = SecureUtil.aes(key);
//        byte[] encrypt = aes.encrypt(JSON.toJSONString(device));
//        String decrypt = aes.decryptStr(encrypt);
//        Device device1 = JSON.parseObject(decrypt).toJavaObject(Device.class);
//        System.out.println(device1);
//    }
    @Test
    void testEmail() throws UnsupportedEncodingException, MessagingException {
        EmailUtil.sendHtmlMail("guoheng85@163.com", "账号审核已通过！", "<p>恭喜您！账号注册审核已成功！</p><hr /><ul><li>本邮件为自动发送, 请勿直接回复</li><li>如非本人发起, 请确认账号是否已被他人盗用</li><li>如有其他问题请发送邮件到 <a href=\"mailto:guoheng85@163.com\">guoheng85@163.com</a></li></ul>");
    }
//
//    @Test
//    void testCurrentDataService() {
//        System.err.println(currentDataService.getByDeviceIdAndDataLevel(6, 0));
//        System.err.println(currentDataService.getByDeviceIdAndDataLevelWithFromDayEndDay(6, 0, "2015-11-20", "2015-11-22"));
//    }
//
//    @Test
//    void testZipUtil() {
//        ZipUtil.zip(FileUtil.file("E:/lushan_server/datasets/test.zip"), true,
//                FileUtil.file("E:\\lushan_server\\datasets\\庐山数据")
//        );
//    }
//
//    @Test
//    void testSendLinkMail() {
//
//        String ROOT = "E:\\lushan_server\\upload\\data";
//        String email = "3084233184@qq.com";
//        File[] files = new File[5];
//        String targetFileName = "2983n4v57345bn.zip";
//        String[] filesName = {"08df9009-4cf8-46f0-9007-798cc20ac73f.jpg",
//                "8a183832-65b0-4e9e-b0a0-61c5fc419f79.jpg",
//                "87b03f7e-5d02-49f8-b433-1453ad07334b.png",
//                "94beba22-c2ac-4611-9c99-b78128edb3bc.webp",
//                "ee65fffe-4277-4be4-9b77-0546fcda3391.jpg"
//        };
//        for (int i = 0; i < files.length; i++) {
//            files[i] = FileUtil.file(ROOT, "08df9009-4cf8-46f0-9007-798cc20ac73f.jpg");
//        }
//        DownLoadFileThread downLoadFileThread = new DownLoadFileThread(email, files, targetFileName);
//        Thread thread = new Thread(downLoadFileThread);
//        thread.start();
//    }
}