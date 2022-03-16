package edu.cuit.lushan.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.alibaba.fastjson.JSON;
import edu.cuit.lushan.config.LushanConfig;
import edu.cuit.lushan.entity.User;
import edu.cuit.lushan.enums.RoleEnum;
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

    public String sign(Integer userId){
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
        return AESUtil.encodeBase64(JSON.toJSONString(authorization));
    }
    public Integer getUserId(String token){

        try {
            Authorization authorization = JSON.parseObject(AESUtil.decodeBase64(token)).toJavaObject(Authorization.class);
            Integer userId = authorization.getUserId();
            return userId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
