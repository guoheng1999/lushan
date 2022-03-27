package edu.cuit.lushan.utils;


import edu.cuit.lushan.entity.User;
import edu.cuit.lushan.enums.RoleEnum;
import edu.cuit.lushan.exception.AuthorizationException;
import edu.cuit.lushan.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Scope("prototype")
@Slf4j
public class UserAgentUtil {
    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    IUserService userService;


    public String sign(Integer userId) {
        return tokenUtil.sign(userId);
    }

    //其中ip的获取方式
    public String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public String getIpAddress(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }
        return ip;
    }

    public String getToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        return token;
    }

    public Integer getUserId(HttpServletRequest request) {
        String token = getToken(request);
        if (token == null) {
            return null;
        }
        Integer userId = tokenUtil.getUserId(token);
        return userId;
    }

    public void verifyUser(User user) throws RuntimeException {
        if (user != null) {
            if (user.getAccountStatus() == null) {
                throw new AuthorizationException("The current account is abnormal!");
            } else {
            }
        } else {
            throw new AuthorizationException("The current account is not found!");
        }
    }

    public void verifyEditorPermission(Integer userId, User user) {
        User operateUser = userService.getById(userId);
        verifyUser(user);
        switch (operateUser.getRoleId()) {
            case 2:
            case 1:
                return;
            case 0:
                if (operateUser.getId() == user.getId()) {
                }
        }
    }

    public boolean hasRole(User user, RoleEnum roleEnum) {
        try {
            //  按照排序先后计算0-user
            System.out.println(roleEnum.ordinal());
            return roleEnum.ordinal() <= user.getRoleId();
        } catch (Exception e) {
            return false;
        }
    }
}