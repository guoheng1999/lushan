package edu.cuit.lushan.config;

import com.louislivi.fastdep.shirojwt.shiro.FastDepShiroJwtAuthorization;
import edu.cuit.lushan.entity.User;
import edu.cuit.lushan.enums.RoleEnum;
import edu.cuit.lushan.service.IUserService;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@Component
@RestControllerAdvice
@ConditionalOnMissingBean(edu.cuit.lushan.shiro.fastdep.FastDepShiroJwtAuthorization.class)
public class FastDepShiroJwtConfig extends FastDepShiroJwtAuthorization {

    @Autowired
    private IUserService userService;

    /***
     *
     * @param userId
     * @return
     * @description 获取用户的权限信息
     */
    @Override
    public SimpleAuthorizationInfo getAuthorizationInfo(String userId) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        User user = userService.getById(userId);
        dealVerifyUserAccount(user);
        dealVerifyUserAccoutStatus(user);
        dealGetUserRole(user, simpleAuthorizationInfo);
        return simpleAuthorizationInfo;
    }

    @Override
    public void shiroFilterFactoryBean(ShiroFilterFactoryBean factoryBean) {

        Map<String, String> filterChainMap = new LinkedHashMap<>();
        filterChainMap.put("/login", "anon");
        filterChainMap.put("/register", "anon");
        filterChainMap.put("/**", "jwt");
        factoryBean.setFilterChainDefinitionMap(filterChainMap);
    }

    private void dealGetUserRole(User user, SimpleAuthorizationInfo simpleAuthorizationInfo){
        switch (user.getRoleId()) {
            case 2:
                simpleAuthorizationInfo.addRole(RoleEnum.ADMIN.toString());
            case 1:
                simpleAuthorizationInfo.addRole(RoleEnum.MANAGER.toString());
            case 0:
                simpleAuthorizationInfo.addRole(RoleEnum.USER.toString());
        }
    }

    /***
     *
     * @param user
     * @description 验证用户是否存在，以及用户的状态是否为正常状态。
     */
    private void dealVerifyUserAccount(User user){
        if (user == null) {
            throw new AuthorizationException("The current account is not found!");
        }
    }

    /***
     * 0-审核状态 1-正常 2-封禁
     * @param user
     */
    private void dealVerifyUserAccoutStatus(User user){
        if (user.getAccountStatus() == null){
            throw new AuthorizationException("The current account is abnormal! Please contact the administrator.");
        }
        switch (user.getAccountStatus()){
            case 0:
                throw new AuthorizationException("The current account is being reviewed!");
            case 1:
                break;
            case 2:
                throw new AuthorizationException("The current account has been banned!");
        }
    }
}