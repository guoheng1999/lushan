package edu.cuit.lushan.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import edu.cuit.lushan.annotation.DataLog;
import edu.cuit.lushan.annotation.RequireRoles;
import edu.cuit.lushan.annotation.WebLog;
import edu.cuit.lushan.entity.User;
import edu.cuit.lushan.enums.RoleEnum;
import edu.cuit.lushan.enums.UserVOEnum;
import edu.cuit.lushan.exception.AuthorizationException;
import edu.cuit.lushan.factory.AbstractFactory;
import edu.cuit.lushan.factory.FactoryProducer;
import edu.cuit.lushan.service.IUserProofService;
import edu.cuit.lushan.service.IUserService;
import edu.cuit.lushan.thread.UserOperateThread;
import edu.cuit.lushan.utils.LushanRedisUtil;
import edu.cuit.lushan.utils.ResponseMessage;
import edu.cuit.lushan.utils.UserAgentUtil;
import edu.cuit.lushan.vo.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
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
@CrossOrigin
public class UserController {

    @Autowired
    IUserService userService;
    @Autowired
    IUserProofService userProofService;
    @Autowired
    UserAgentUtil userAgentUtil;
    AbstractFactory<User> abstractFactory = FactoryProducer.getFactory(FactoryProducer.FactoryName.USER);
    @Autowired
    LushanRedisUtil redisUtil;

    @ApiOperation(value = "获取所有已通过的用户", tags = {"用户管理"})
    @GetMapping("/all")
    @RequireRoles(value = RoleEnum.MANAGER)
//    @WebLog
    public ResponseMessage getAll() {
        List list = new ArrayList();
        userService.selectAllUser().forEach(
                (e) -> list.add(abstractFactory.buildVOByEntity(e, UserVOEnum.USER_INFO.name()))
        );
        return ResponseMessage.success(list);
    }

    @DataLog
    @ApiOperation(value = "查询所有的待审核用户", tags = {"用户管理"})
    @GetMapping("/reviewed/all")
//    @WebLog
    @RequireRoles(RoleEnum.MANAGER)
    public ResponseMessage getAllUnderReviewed() {
        return ResponseMessage.success(userService.selectAllUnderReviewed());
    }


    @DataLog
    @ApiOperation(value = "查询所有被封禁的用户", tags = {"用户管理"})
    @GetMapping("/banned/all")
//    @WebLog
    @RequireRoles(RoleEnum.MANAGER)
    public ResponseMessage getAllBanedUser() {
        return ResponseMessage.success(userService.selectAllBanedUser());
    }

    @ApiOperation(value = "获取一个用户的信息", tags = {"用户管理"})
    @GetMapping("/{email}")
//    @WebLog
    public ResponseMessage getOne(@PathVariable String email) {
        User user = userService.selectByEmail(email);
        if (user == null) {
            return ResponseMessage.nullError(email);
        }
        return ResponseMessage.success(
                abstractFactory.buildVOByEntity(user, UserVOEnum.USER_INFO.name())
        );
    }

    @ApiOperation(value = "添加用户", tags = {"用户管理"})
    @PostMapping("/")
    @DataLog
//    @WebLog
    public ResponseMessage add(@RequestBody RegisterVO registerVO,
                               HttpServletRequest request) {
        if (registerVO == null || BeanUtil.hasNullField(registerVO)) {
            return ResponseMessage.nullError(registerVO);
        }
        if (userService.selectByEmail(registerVO.getEmail()) != null) {
            return ResponseMessage.existsError(registerVO);
        }
        User user = abstractFactory.buildEntityByVO(new User(), registerVO);
        user.setModifyUserId(userAgentUtil.getUserId(request));
        if (userService.save(user)) {
            return ResponseMessage.success(abstractFactory.buildVOByEntity(user, UserVOEnum.USER_INFO.name()));
        }
        return ResponseMessage.serverError(user);
    }

    @DataLog
    @ApiOperation(value = "更新用户基本信息", tags = {"用户管理"})
    @PutMapping("/{email}")
//    @WebLog
    public ResponseMessage update(@PathVariable String email,
                                  @RequestBody UserInfoVO releaseVersionUserInfo,
                                  HttpServletRequest request) {
        User oldUser = userService.selectByEmail(email);
        verifyPermission(request, oldUser);

        BeanUtil.copyProperties(releaseVersionUserInfo, oldUser,
                CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        if (userService.updateById(oldUser)) {
            return ResponseMessage.success(releaseVersionUserInfo);
        } else {
            return ResponseMessage.serverError(releaseVersionUserInfo);
        }
    }


    @DataLog
    @ApiOperation(value = "修改用户密码", tags = {"用户管理"})
    @PutMapping("/password")
//    @WebLog(hasToken = false)
    public ResponseMessage changePassword(
            @RequestBody UserPasswordVO userPasswordVO,
            HttpServletRequest request) {
        if (userPasswordVO == null || BeanUtil.hasNullField(userPasswordVO, "code")) {
            return ResponseMessage.nullError(userPasswordVO);
        }
        User oldUser = userService.selectByEmail(userPasswordVO.getEmail());
        // 验证操作用户权限
//        verifyPermission(request, oldUser);
        // 查看用户是否携带code验证码 如果未携带判断是否具有管理权限。
        if (userPasswordVO.getCode() == null) {
            if (!userAgentUtil.hasRole(userService.getById(userAgentUtil.getUserId(request)),
                    RoleEnum.MANAGER)) {
                throw new AuthorizationException(String.format("无操作权限，请联系管理员！", RoleEnum.MANAGER.name()));
            }
        }
        // 从redis数据库中取出存放的code
        String code = (String) redisUtil.get(userPasswordVO.getEmail(), String.class);
        // code 为空或code比对不成功返回出错
        if (code == null || !code.equals(userPasswordVO.getCode())) {
            throw new AuthorizationException("验证码错误！");
        }
        BeanUtil.copyProperties(userPasswordVO, oldUser,
                CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        if (userService.updateById(oldUser)) {
            return ResponseMessage.success(userPasswordVO);
        } else {
            return ResponseMessage.serverError(userPasswordVO);
        }
    }

    @DataLog
    @ApiOperation(value = "修改用户组织机构", tags = {"用户管理"})
    @PutMapping("/organization")
//    @WebLog
    public ResponseMessage changeUserOrganization(@RequestBody UserOrganizationVO userOrganizationVO,
                                                  HttpServletRequest request) {
        User oldUser = userService.selectByEmail(userOrganizationVO.getEmail());
        verifyPermission(request, oldUser);
        BeanUtil.copyProperties(userOrganizationVO, oldUser,
                CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        if (userService.updateById(oldUser)) {
            return ResponseMessage.success(userOrganizationVO);
        } else {
            return ResponseMessage.serverError(userOrganizationVO);
        }
    }

    @DataLog
    @ApiOperation(value = "修改用户权限", tags = {"用户管理"})
    @PutMapping("/authorization")
//    @WebLog
    public ResponseMessage changeUserAuthorization(@RequestBody UserAuthorizationVO userAuthorizationVO,
                                                   HttpServletRequest request) {
        User oldUser = userService.selectByEmail(userAuthorizationVO.getEmail());
        verifyPermission(request, oldUser);
        BeanUtil.copyProperties(userAuthorizationVO, oldUser,
                CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));

        if (userService.updateById(oldUser)) {
            return ResponseMessage.success(userAuthorizationVO);
        } else {
            return ResponseMessage.serverError(userAuthorizationVO);
        }
    }


    @ApiOperation(value = "逻辑删除用户信息", tags = {"用户管理"})
    @DeleteMapping("/{email}")
    @DataLog
//    @WebLog
    public ResponseMessage delete(@PathVariable String email, HttpServletRequest request) {
        // 判断userId是否为null或空字符串
        if (StrUtil.isEmpty(email)) {
            return ResponseMessage.nullError(email);
        }
        User user = userService.selectByEmail(email);
        // 验证是否对当前操作对象有权限
        verifyPermission(request, user);
        if (user == null) {
            return ResponseMessage.notFound(email);
        }
        // 更新的是verifyPermission中记录的修改人。
        userService.updateById(user);
        // 记录修改人之后直接remove
        if (userService.removeById(user.getId())) {
            return ResponseMessage.successCodeMsgData(2000, String.format("用户{}的账号已删除", user.getRealName()), user);
        } else {
            return ResponseMessage.serverError(email);
        }

    }

    @ApiOperation(value = "物理删除用户信息", tags = {"用户管理"})
    @DeleteMapping("/physics/{email}")
    @DataLog
//    @WebLog
    @RequireRoles(RoleEnum.MANAGER)
    public ResponseMessage deletePhysics(@PathVariable String email, HttpServletRequest request) {
        // 判断userId是否为null或空字符串
        if (StrUtil.isEmpty(email)) {
            return ResponseMessage.nullError(email);
        }
        // 查询是否存在待删用户
        User user = userService.selectByEmail(email);
        if (user == null) {
            return ResponseMessage.notFound(email);
        } else {

            // 验证是否对当前操作对象有权限
            verifyPermission(request, user);
            Thread thread = new Thread(new UserOperateThread(email, userProofService, userService));
            thread.start();
            return ResponseMessage.successCodeMsgData(2000, String.format("用户{}的账号已删除", user.getRealName()), user);
        }
    }


    @ApiOperation(value = "用户登录接口", tags = {"用户管理"})
    @PostMapping("/login")
//    @WebLog
    public ResponseMessage login(HttpServletResponse response, LoginVO loginVO) {
        // 判断请求数据是否为空,包括是否为空字符。
        if (loginVO == null || StrUtil.isEmpty(loginVO.getEmail()) || StrUtil.isEmpty(loginVO.getPassword())) {
            return ResponseMessage.nullError(loginVO);
        }
        // 通过email查找用户
        User user = userService.loginByEmail(loginVO.getEmail(), loginVO.getPassword());
        // 若user为空则直接返回未找到该数据。
        if (user == null) {
            return ResponseMessage.notFound(loginVO);
        }
        // 添加Token
        loginVO.setToken(userAgentUtil.sign(user.getId()));
        return ResponseMessage.success(loginVO);
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

