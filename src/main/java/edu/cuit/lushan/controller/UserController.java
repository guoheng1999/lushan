package edu.cuit.lushan.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import edu.cuit.lushan.annotation.DataLog;
import edu.cuit.lushan.entity.User;
import edu.cuit.lushan.enums.UserVOEnum;
import edu.cuit.lushan.factory.AbstractFactory;
import edu.cuit.lushan.factory.FactoryProducer;
import edu.cuit.lushan.factory.UserVOFactory;
import edu.cuit.lushan.service.IUserService;
import edu.cuit.lushan.utils.ResponseMessage;
import edu.cuit.lushan.utils.UserAgentUtil;
import edu.cuit.lushan.vo.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Guoheng
 * @since 2022-01-27
 */
@RestController
@RequestMapping("/user")
@ApiModel(value = "用户管理", description = "对用户的增删改查操作")
@RequiresRoles({"USER"})
public class UserController {

    @Autowired
    IUserService userService;
    @Autowired
    UserAgentUtil userAgentUtil;
    AbstractFactory<User> abstractFactory = FactoryProducer.getFactory(FactoryProducer.FactoryName.USER);

    @ApiOperation(value = "获取所有用户", tags = {"用户管理"})
    @GetMapping("/")
    public ResponseMessage getAll() {
        List list = new ArrayList();
        userService.list().forEach(
                (e) -> list.add(abstractFactory.buildVOByEntity(e, UserVOEnum.USER_INFO.name()))
        );
        return ResponseMessage.success(list);
    }

    @ApiOperation(value = "获取一个用户的信息", tags = {"用户管理"})
    @GetMapping("/{userId}")
    public ResponseMessage getOne(@PathVariable String userId) {
        User user = userService.getById(userId);
        if (user == null){
            return ResponseMessage.nullError(userId);
        }
        return ResponseMessage.success(
                abstractFactory.buildVOByEntity(user, UserVOEnum.USER_INFO.name())
        );
    }

    @ApiOperation(value = "添加用户", tags = {"用户管理"})
    @PostMapping("/")
    @DataLog
    @RequiresRoles(value = {"ADMIN"},logical = Logical.OR)
    public ResponseMessage add(@RequestBody RegisterVO registerVO,
                               HttpServletRequest request) {
        if (registerVO == null || BeanUtil.hasNullField(registerVO)){
            return ResponseMessage.nullError(registerVO);
        }
        if (userService.selectByEmail(registerVO.getEmail()) != null) {
            return ResponseMessage.existsError(registerVO);
        }
        User user = abstractFactory.buildEntityByVO(new User(), registerVO);
        user.setModifyUserId(userAgentUtil.getUserId(request));
        if (userService.save(user)) {
            return ResponseMessage.success(user);
        }
        return ResponseMessage.serverError(user);
    }

    @DataLog
    @ApiOperation(value = "更新用户基本信息", tags = {"用户管理"})
    @PutMapping("/{userId}")
    public ResponseMessage update(@PathVariable String userId,
                                  @RequestBody UserInfoVO releaseVersionUserInfo,
                                  HttpServletRequest request) {
        User oldUser = userService.getById(userId);
        verifyPermission(request, oldUser);

        BeanUtil.copyProperties(releaseVersionUserInfo,oldUser,
                CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        if (userService.updateById(oldUser)) {
            return ResponseMessage.success(releaseVersionUserInfo);
        }else {
            return ResponseMessage.serverError(releaseVersionUserInfo);
        }
    }


    @DataLog
    @ApiOperation(value = "修改用户密码", tags = {"用户管理"})
    @PutMapping("/password")
    public ResponseMessage changePassword(
                                          @RequestBody UserPasswordVO userPasswordVO,
                                          HttpServletRequest request) {
        if (userPasswordVO == null || BeanUtil.hasNullField(userPasswordVO)){
            return ResponseMessage.nullError(userPasswordVO);
        }
        User oldUser =  userService.getById(userPasswordVO.getId());
        // 验证操作用户权限
        verifyPermission(request, oldUser);
        BeanUtil.copyProperties(userPasswordVO,oldUser,
                CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        if (userService.updateById(oldUser)) {
            return ResponseMessage.success(userPasswordVO);
        }else {
            return ResponseMessage.serverError(userPasswordVO);
        }
    }

    @DataLog
    @ApiOperation(value = "修改用户组织机构", tags = {"用户管理"})
    @PutMapping("/{userId}/organization")
    public ResponseMessage changeUserOrganization(@PathVariable String userId,
                                                  @RequestBody UserOrganizationVO userOrganizationVO,
                                                  HttpServletRequest request) {
        User oldUser = userService.getById(userId);
        verifyPermission(request, oldUser);
        BeanUtil.copyProperties(userOrganizationVO,oldUser,
                CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        if (userService.updateById(oldUser)) {
            return ResponseMessage.success(userOrganizationVO);
        }else {
            return ResponseMessage.serverError(userOrganizationVO);
        }
    }

    @DataLog
    @ApiOperation(value = "修改用户权限", tags = {"用户管理"})
    @PutMapping("/{userId}/authorization")
    public ResponseMessage changeUserAuthorization(@PathVariable Integer userId,
                                                   @RequestBody UserAuthorizationVO userAuthorizationVO,
                                                   HttpServletRequest request) {
        User oldUser =userService.getById(userId);
        verifyPermission(request, oldUser);
        BeanUtil.copyProperties(userAuthorizationVO,oldUser,
                CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        if (userService.updateById(oldUser)) {
            return ResponseMessage.success(userAuthorizationVO);
        }else {
            return ResponseMessage.serverError(userAuthorizationVO);
        }
    }


    @ApiOperation(value = "删除用户信息", tags = {"用户管理"})
    @DeleteMapping("/{userId}")
    @DataLog
    public ResponseMessage delete(@PathVariable String userId, HttpServletRequest request) {
        // 判断userId是否为null或空字符串
        if (StrUtil.isEmpty(userId)){
            return ResponseMessage.nullError(userId);
        }
        User user = userService.getById(userId);
        // 验证是否对当前操作对象有权限
        verifyPermission(request, user);
        if (user == null) {
            return ResponseMessage.notFound(userId);
        }
        // 更新的是verifyPermission中记录的修改人。
        userService.updateById(user);
        // 记录修改人之后直接remove
        if (userService.removeById(user.getId())) {
            return ResponseMessage.successCodeMsgData(2000, "User deleted successfully!", user);
        } else {
            return ResponseMessage.serverError(userId);
        }

    }


    @ApiOperation(value = "用户登录接口", tags = {"用户管理"})
    @PostMapping("/login")
    public ResponseMessage login(HttpServletResponse response, LoginVO loginVO) {
        // 判断请求数据是否为空,包括是否为空字符。
        if (loginVO == null || StrUtil.isEmpty(loginVO.getEmail()) || StrUtil.isEmpty(loginVO.getPassword())){
            return ResponseMessage.nullError(loginVO);
        }
        // 通过email查找用户
        User user = userService.loginByEmail(loginVO.getEmail(), loginVO.getPassword());
        // 若user为空则直接返回未找到该数据。
        if (user == null) {
            return ResponseMessage.notFound(loginVO);
        }
        // 添加Token至Headers
        response.addHeader("token", userAgentUtil.sign(user.getId()));
        return ResponseMessage.success(abstractFactory.buildVOByEntity(user, UserVOEnum.USER_INFO.name()));
    }

    /***
     *
     * @param request 操作人
     * @param user 将要被操作的用户
     * @return
     */
    private void verifyPermission(HttpServletRequest request, User user) {
        User operateUser = userService.getById(userAgentUtil.getUserId(request));
        userAgentUtil.verifyEditorPermission(operateUser.getId(), user);
        user.setModifyUserId(userAgentUtil.getUserId(request));
    }
}

