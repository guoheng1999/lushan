package edu.cuit.lushan.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;

import java.util.HashMap;
import java.util.Map;

public class LoginUtil {
    public final static Integer HALF_HOUR = 30;
    public final static Integer HOUR = HALF_HOUR * 2;
    public final static Integer DAY = HOUR * 24;
    public final static Integer WEEK = DAY * 7;
    private final static String KEY = "cuit1952";

    public static String createToken(String userId, String email, String password, Integer outmodedMinutes) {
        DateTime now = DateTime.now();
        DateTime newTime = now.offsetNew(DateField.MINUTE, outmodedMinutes);

        Map<String, Object> payload = new HashMap<String, Object>();
        //签发时间
        payload.put(JWTPayload.ISSUED_AT, now);
        //过期时间
        payload.put(JWTPayload.EXPIRES_AT, newTime);
        //生效时间
        payload.put(JWTPayload.NOT_BEFORE, now);
        //载荷
        payload.put("username", userId);
        payload.put("email", email);
        payload.put("password", password);
        String token = JWTUtil.createToken(payload, KEY.getBytes());
        return token;
    }

    public static boolean verify(String token, String email, String userId, String password) {
        JWT jwt = JWTUtil.parseToken(token);
        boolean verifyKey = jwt.setKey(KEY.getBytes()).verify();
        boolean verifyTime = jwt.validate(0);
        boolean verifyEmail = email.equals(jwt.getPayload("email"));
        boolean verifyUserId = userId.equals(jwt.getPayload("username"));
        boolean verifyPassword = userId.equals(jwt.getPayload("password"));
        return verifyKey && verifyTime && verifyEmail && verifyUserId && verifyPassword;
    }

    public static boolean verify(String token, String email, String password) {
        JWT jwt = JWTUtil.parseToken(token);
        boolean verifyKey = jwt.setKey(KEY.getBytes()).verify();
        boolean verifyTime = jwt.validate(0);
        boolean verifyEmail = email.equals(jwt.getPayload("email"));
        return verifyKey && verifyTime && verifyEmail;
    }

}
