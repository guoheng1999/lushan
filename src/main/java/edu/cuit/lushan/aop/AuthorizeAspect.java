package edu.cuit.lushan.aop;


import edu.cuit.lushan.annotation.RequireRoles;
import edu.cuit.lushan.entity.User;
import edu.cuit.lushan.exception.AuthorizationException;
import edu.cuit.lushan.service.IUserService;
import edu.cuit.lushan.utils.UserAgentUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

//import edu.cuit.lushan.utils.LushanRedisUtil;
//import edu.cuit.lushan.utils.RedisUtil;

@Aspect
@Component
@Slf4j
public class AuthorizeAspect {
    //    @Autowired
//    LushanRedisUtil<Authorization> redisUtil;
    @Autowired
    UserAgentUtil userAgentUtil;
    @Autowired
    IUserService userService;

    @PostConstruct
    public void verifyUser() {

    }

    @Before("@annotation(roles)")
    public void doVerifyUser(RequireRoles roles) {
        //获取request对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Integer userId = userAgentUtil.getUserId(request);
        User user = userService.getById(userId);
        userAgentUtil.verifyUser(user);
        if (!userAgentUtil.hasRole(user, roles.value())) {
            throw new AuthorizationException(String.format("您的权限不能进行该操作，请联系管理员。", roles.value().name()), userId);
        }
    }
}
