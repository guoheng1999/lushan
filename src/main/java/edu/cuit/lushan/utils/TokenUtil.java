package edu.cuit.lushan.utils;

import com.alibaba.fastjson.JSON;
import edu.cuit.lushan.config.LushanConfig;
import edu.cuit.lushan.entity.User;
import edu.cuit.lushan.enums.RoleEnum;
import edu.cuit.lushan.exception.MyRuntimeException;
import edu.cuit.lushan.service.IUserService;
import edu.cuit.lushan.vo.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class TokenUtil {
    @Autowired
    LushanConfig lushanConfig;
    @Autowired
    IUserService userService;
    @Autowired
    LushanRedisUtil redisUtil;

    public String sign(Integer userId) {
        if (userId == null) {
            return null;
        }
        String token = (String) redisUtil.get(userId.toString(), String.class);
        // 如果token存在，则刷新当前token的过期时间
        if (token != null) {
            redisUtil.save(userId.toString(), token);
            return token;
        }else {
            // 如果token不存在，则生成新的token
            User user = userService.getById(userId);
            Set<String> roleSet = new HashSet<>();
            switch (user.getRoleId()) {
                case 3:
                    roleSet.add(RoleEnum.ADMIN.name());
                case 2:
                    roleSet.add(RoleEnum.MANAGER.name());
                case 1:
                    roleSet.add(RoleEnum.VIP.name());
                case 0:
                    roleSet.add(RoleEnum.USER.name());
            }
            Authorization authorization = Authorization.builder()
                    .roles(roleSet)
                    .userId(userId)
                    .build();
            token = AESUtil.encodeBase64(JSON.toJSONString(authorization));
            redisUtil.save(userId.toString(), token, 4l);
            return token;
        }
    }

    // token是否有效
    public boolean isValid(String token) {
        if (token == null) {
            return false;
        }
        Authorization authorization = JSON.parseObject(AESUtil.decodeBase64(token), Authorization.class);
        if (authorization == null) {
            return false;
        }
        String redisToken = (String) redisUtil.get(authorization.getUserId().toString(), String.class);
        if (redisToken == null) {
            return false;
        }
        return token.equals(redisToken);
    }

    // 刷新token过期时间
    public void refresh(String token) {
        if (token == null) {
            return;
        }
        Authorization authorization = JSON.parseObject(AESUtil.decodeBase64(token), Authorization.class);
        if (authorization == null) {
            return;
        }
        redisUtil.save(authorization.getUserId().toString(), token, 4l);
    }

    public Integer getUserId(String token) {
        Authorization authorization = JSON.parseObject(AESUtil.decodeBase64(token)).toJavaObject(Authorization.class);
        Integer userId = authorization.getUserId();
        if (isValid(token)) {
            refresh(token);
            return userId;
        }else {
            throw new MyRuntimeException("token无效，请重新登录。");
        }
    }
}
