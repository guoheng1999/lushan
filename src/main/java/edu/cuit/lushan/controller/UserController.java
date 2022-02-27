package edu.cuit.lushan.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.louislivi.fastdep.shirojwt.jwt.JwtUtil;
import edu.cuit.lushan.annotation.DataLog;
import edu.cuit.lushan.entity.User;
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

    @ApiOperation(value = "获取所有用户", tags = {"用户管理"})
    @GetMapping("/")
    public ResponseMessage getAll() {
        return ResponseMessage.success(userService.list());
    }

    @ApiOperation(value = "获取一个用户的信息", tags = {"用户管理"})
    @GetMapping("/{userId}")
    public ResponseMessage getOne(@PathVariable String userId) {
        User user = userService.getById(userId);
        return ResponseMessage.success(user);
    }

    @ApiOperation(value = "添加用户", tags = {"用户管理"})
    @PostMapping("/")
    @DataLog
    @RequiresRoles(value = {"ADMIN"},logical = Logical.OR)
    public ResponseMessage add(@RequestBody User user, HttpServletRequest request) {
        user.setModifyUserId(userAgentUtil.getUserId(request));
        userService.save(user);
        return ResponseMessage.success(user);
    }

    @DataLog
    @ApiOperation(value = "更新用户基本信息", tags = {"用户管理"})
    @PutMapping("/{userId}")
    public ResponseMessage update(@PathVariable String userId, @RequestBody UserInfoVO releaseVersionUserInfo, HttpServletRequest request) {
        User oldUser = userService.getById(userId);
        oldUser.setModifyUserId(userAgentUtil.getUserId(request));
        if (oldUser == null){
            return ResponseMessage.successCodeMsgData(2404, "User not found!", releaseVersionUserInfo);
        }
        BeanUtil.copyProperties(releaseVersionUserInfo,oldUser,
                CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        if (userService.updateById(oldUser)) {
            return ResponseMessage.success(releaseVersionUserInfo);
        }else {
            return ResponseMessage.error("Server Error!");
        }
    }


    @DataLog
    @ApiOperation(value = "修改用户密码", tags = {"用户管理"})
    @PutMapping("/{userId}/password")
    public ResponseMessage changePassword(@PathVariable String userId, @RequestBody UserPasswordVO userPasswordVO, HttpServletRequest request) {
        User oldUser = userService.getById(userId);
        oldUser.setModifyUserId(userAgentUtil.getUserId(request));
        if (oldUser == null){
            return ResponseMessage.successCodeMsgData(2404, "User not found!", userPasswordVO);
        }
        BeanUtil.copyProperties(userPasswordVO,oldUser,
                CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        if (userService.updateById(oldUser)) {
            return ResponseMessage.success(userPasswordVO);
        }else {
            return ResponseMessage.error("Server Error!");
        }
    }

    @DataLog
    @ApiOperation(value = "修改用户组织机构", tags = {"用户管理"})
    @PutMapping("/{userId}/organization")
    public ResponseMessage changeUserOrganization(@PathVariable String userId, @RequestBody UserOrganizationVO userOrganizationVO, HttpServletRequest request) {
        User oldUser = userService.getById(userId);
        oldUser.setModifyUserId(userAgentUtil.getUserId(request));
        if (oldUser == null){
            return ResponseMessage.successCodeMsgData(2404, "User not found!", userOrganizationVO);
        }
        BeanUtil.copyProperties(userOrganizationVO,oldUser,
                CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        if (userService.updateById(oldUser)) {
            return ResponseMessage.success(userOrganizationVO);
        }else {
            return ResponseMessage.error("Server Error!");
        }
    }

    @DataLog
    @ApiOperation(value = "修改用户权限", tags = {"用户管理"})
    @PutMapping("/{userId}/authorization")
    public ResponseMessage changeUserAuthorization(@PathVariable String userId, HttpServletRequest request,@RequestBody UserAuthorizationVO userAuthorizationVO) {
        User oldUser = userService.getById(userId);
        oldUser.setModifyUserId(userAgentUtil.getUserId(request));
        if (oldUser == null){
            return ResponseMessage.successCodeMsgData(2404, "User not found!", userAuthorizationVO);
        }
        BeanUtil.copyProperties(userAuthorizationVO,oldUser,
                CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        if (userService.updateById(oldUser)) {
            return ResponseMessage.success(userAuthorizationVO);
        }else {
            return ResponseMessage.error("Server Error!");
        }
    }


    @ApiOperation(value = "删除用户信息", tags = {"用户管理"})
    @DeleteMapping("/{userId}")
    @DataLog
    public ResponseMessage delete(@PathVariable String userId, HttpServletRequest request) {
        User user = userService.getById(userId);
        user.setModifyUserId(userAgentUtil.getUserId(request));
        userService.updateById(user);
        if (user == null) {
            return ResponseMessage.successCodeMsg(2404, "User Not Found!");
        }
        if (userService.removeById(user.getId())) {
            return ResponseMessage.successCodeMsgData(2000, "User deleted successfully!", user);
        } else {
            return ResponseMessage.successCodeMsgData(2001, "User failed to deleted!", user);
        }

    }

    @ApiOperation(value = "用户登录接口", tags = {"用户管理"})
    @PostMapping("/login")
    public ResponseMessage login(HttpServletResponse response, LoginVO loginVO) {
        if ("".equals(loginVO.getEmail()) || "".equals(loginVO.getPassword()) || loginVO.getEmail() == null || loginVO.getPassword() == null) {
            return ResponseMessage.successCodeMsgData(2001, "The email and password fields cannot be empty!", loginVO);
        }
        User user = userService.loginByEmail(loginVO.getEmail(), loginVO.getPassword());
        if (user == null) {
            return ResponseMessage.successCodeMsgData(2404, "User is not found or password is not validated!", loginVO);
        }
        response.addHeader("token", userAgentUtil.sign(user.getId()));
        return ResponseMessage.success(user);
    }

}

