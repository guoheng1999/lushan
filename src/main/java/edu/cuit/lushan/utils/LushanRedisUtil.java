package edu.cuit.lushan.utils;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import edu.cuit.lushan.entity.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class LushanRedisUtil<T> {
    public final static Long HOUR = 1L;
    public final static Long HALF_DAY = HOUR * 12;
    public final static Long DAY = HALF_DAY * 2;
    public final static Long WEEK = DAY * 7;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    public boolean save(String key, T value){
        return this.save(key, value, HOUR);
    }

    /***
     *
     * @param key
     * @param value
     * @param expireTime{TimeUnit == HOURS}
     * @return
     */
    public boolean save(String key, T value, Long expireTime){
        String jsonStr = JSON.toJSONString(value);
        stringRedisTemplate.opsForValue().set(key, jsonStr, expireTime, TimeUnit.HOURS);
        if (StrUtil.isEmpty(stringRedisTemplate.opsForValue().get(key))){
            return false;
        }else {
            return true;
        }
    }
    public T get(String key, Class<T> clazz){
        String getStr = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isEmpty(getStr)) {
            return null;
        }
        T object = JSON.parseObject(getStr, clazz);
        System.out.println("getDevice = " + object);
        return object;
    }
    public boolean delete(String key){
        return stringRedisTemplate.delete(key);
    }

}
