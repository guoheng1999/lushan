package edu.cuit.lushan.factory;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import edu.cuit.lushan.entity.User;
import edu.cuit.lushan.enums.UserVOEnum;
import edu.cuit.lushan.vo.*;

public class UserVOFactory extends AbstractFactory<User> {
    protected UserVOFactory(){

    }
    @Override
    public User buildEntityByVO(User user, Object vo) {
        if (vo == null || BeanUtil.hasNullField(vo)){
            return null;
        }else {
            BeanUtil.copyProperties(vo, user, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        }
        return user;
    }

    @Override
    public Object buildVOByEntity(User entity, String name) {
        CopyOptions copyOptions = CopyOptions.create().setIgnoreError(true).setIgnoreNullValue(true);
        switch (UserVOEnum.valueOf(name)){
            case LOGIN:
                LoginVO loginVO = new LoginVO();
                BeanUtil.copyProperties(entity, loginVO, copyOptions);
                return loginVO;
            case REGISTER:
                RegisterVO registerVO = new RegisterVO();
                BeanUtil.copyProperties(entity, registerVO, copyOptions);
                return registerVO;
            case USER_EDU:
                UserEduVO userEduVO = new UserEduVO();
                BeanUtil.copyProperties(entity, userEduVO, copyOptions);
                return userEduVO;
            case USER_INFO:
                UserInfoVO userInfoVO = new UserInfoVO();
                BeanUtil.copyProperties(entity, userInfoVO, copyOptions);
                return userInfoVO;
            case USER_PASSWORD:
                UserPasswordVO userPasswordVO = new UserPasswordVO();
                BeanUtil.copyProperties(entity, userPasswordVO, copyOptions);
                return userPasswordVO;
            case USER_ORGANIZATION:
                UserOrganizationVO userOrganizationVO = new UserOrganizationVO();
                BeanUtil.copyProperties(entity, userOrganizationVO, copyOptions);
                return userOrganizationVO;
            case USER_AUTHORIZATION:
                UserAuthorizationVO userAuthorizationVO = new UserAuthorizationVO();
                BeanUtil.copyProperties(entity, userAuthorizationVO, copyOptions);
                return userAuthorizationVO;
        }
        return null;
    }
}
