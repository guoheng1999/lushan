package edu.cuit.lushan.controller;

import cn.hutool.core.bean.BeanUtil;
import edu.cuit.lushan.annotation.DataLog;
import edu.cuit.lushan.annotation.RequireRoles;
import edu.cuit.lushan.entity.User;
import edu.cuit.lushan.enums.RoleEnum;
import edu.cuit.lushan.exception.MyRuntimeException;
import edu.cuit.lushan.factory.AbstractFactory;
import edu.cuit.lushan.factory.FactoryProducer;
import edu.cuit.lushan.service.IUserService;
import edu.cuit.lushan.utils.EmailUtil;
import edu.cuit.lushan.utils.ResponseMessage;
import edu.cuit.lushan.utils.UserAgentUtil;
import edu.cuit.lushan.vo.EmailVO;
import edu.cuit.lushan.vo.LoginVO;
import edu.cuit.lushan.vo.RegisterVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@ApiModel(value = "用户登陆注册中心", description = "用户登录")
@CrossOrigin
public class AccessHandler {
    @Autowired
    IUserService userService;
    @Autowired
    UserAgentUtil userAgentUtil;

    AbstractFactory<User> abstractFactory = FactoryProducer.getFactory(FactoryProducer.FactoryName.USER);

    @DataLog
    @ApiOperation(value = "用户登录接口", tags = {"权限获取管理"})
    @PostMapping("/login")
    public ResponseMessage login(HttpServletResponse response, @RequestBody LoginVO loginVO) {
        if ("".equals(loginVO.getEmail()) ||
                "".equals(loginVO.getPassword()) ||
                loginVO.getEmail() == null ||
                loginVO.getPassword() == null) {
            return ResponseMessage.successCodeMsgData(2001, "请填写邮箱和密码进行登录。", loginVO);
        }
        User user = userService.loginByEmail(loginVO.getEmail(), loginVO.getPassword());
        if (user == null) {
            return ResponseMessage.successCodeMsgData(2404, "账号或密码错误。", loginVO);
        }
        loginVO.setToken(userAgentUtil.sign(user.getId()));
        return ResponseMessage.success(loginVO);
    }

    @DataLog
    @ApiOperation(value = "管理员登陆接口", tags = {"权限获取管理"})
    @PostMapping("/manager/login")
    public ResponseMessage managerLogin(HttpServletResponse response, @RequestBody LoginVO loginVO) {
        if ("".equals(loginVO.getEmail()) ||
                "".equals(loginVO.getPassword()) ||
                loginVO.getEmail() == null ||
                loginVO.getPassword() == null) {
            return ResponseMessage.successCodeMsgData(2001, "请填写邮箱和密码进行登录。", loginVO);
        }
        User user = userService.loginByEmail(loginVO.getEmail(), loginVO.getPassword());
        if (user == null) {
            return ResponseMessage.successCodeMsgData(2404, "账号或密码错误。", loginVO);
        }
        if (user.getRoleId() < RoleEnum.MANAGER.getCode()) {
            return ResponseMessage.successCodeMsgData(2500, "您的权限不能登录管理系统。", loginVO);
        }
        loginVO.setToken(userAgentUtil.sign(user.getId()));
        return ResponseMessage.success(loginVO);
    }

    @ApiOperation(value = "用户注册接口", tags = {"权限获取管理"})
    @PostMapping("/register")
    @DataLog
    public ResponseMessage register(@RequestBody RegisterVO registerVO) {
        if (registerVO == null || BeanUtil.hasNullField(registerVO)) {
            return ResponseMessage.errorMsg(2500, "请填写您的注册信息。", registerVO);
        }
        if (userService.selectByEmail(registerVO.getEmail()) != null) {
            return ResponseMessage.existsError(registerVO);
        }
        AbstractFactory<User> abstractFactory = FactoryProducer.getFactory(FactoryProducer.FactoryName.USER);
        User user = abstractFactory.buildEntityByVO(new User(), registerVO);
        userService.save(user);
        user = userService.selectByEmail(user.getEmail());
        user.setModifyUserId(user.getId());
        if (userService.updateById(user)) {
            ResponseMessage responseMessage = ResponseMessage.success(registerVO);
            responseMessage.setToken(userAgentUtil.sign(user.getId()));
            return responseMessage;
        } else {
            throw new MyRuntimeException(registerVO);
        }
    }

    @ApiOperation(value = "为用户发送邮件", tags = {"权限获取管理"})
    @PostMapping("/email")
    @DataLog
    @RequireRoles(RoleEnum.MANAGER)
    public ResponseMessage emailTo(@RequestBody EmailVO emailVO) {
        try {
            if (emailVO.getType() == 0) {
                EmailUtil.sendRegisterSuccessEmail(emailVO.getEmail());
            } else if (emailVO.getType() == 1) {
                EmailUtil.sendRegisterFailureEmail(emailVO.getEmail(), emailVO.getMessage());
            } else if (emailVO.getType() == 2) {
                EmailUtil.sendChangePasswordEmail(emailVO.getEmail());
            }
            return ResponseMessage.success(emailVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.errorMsg(2500, e.getMessage(), emailVO);
        }
    }


    @ApiOperation(value = "为用户发送邮件", tags = {"权限获取管理"})
    @PostMapping("/email/resetPassword")
    @DataLog
    public ResponseMessage emailCode(@RequestBody EmailVO emailVO) {
        try {
            if (emailVO.getType() == 2) {
                EmailUtil.sendChangePasswordEmail(emailVO.getEmail());
                return ResponseMessage.success(emailVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.errorMsg(2500, e.getMessage(), emailVO);
        }
        return ResponseMessage.errorMsg(2500, "系统未知错误，请重启浏览器或与管理员联系。", emailVO);
    }
}
