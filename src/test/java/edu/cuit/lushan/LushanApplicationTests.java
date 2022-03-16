package edu.cuit.lushan;

import cn.hutool.Hutool;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import edu.cuit.lushan.entity.Device;
import edu.cuit.lushan.entity.User;
import edu.cuit.lushan.utils.LushanRedisUtil;
import edu.cuit.lushan.vo.UserInfoVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.crypto.SecretKey;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class LushanApplicationTests {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    LushanRedisUtil<Device> lushanRedisUtil;
    @Test
    void contextLoads() {
    }
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
}
