package edu.cuit.lushan.controller;

import cn.hutool.core.bean.BeanUtil;
import edu.cuit.lushan.annotation.DataLog;
import edu.cuit.lushan.annotation.RequireRoles;
import edu.cuit.lushan.annotation.WebLog;
import edu.cuit.lushan.entity.User;
import edu.cuit.lushan.enums.RoleEnum;
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
    @WebLog(hasToken = false)
    public ResponseMessage login(HttpServletResponse response, @RequestBody LoginVO loginVO) {
        if ("".equals(loginVO.getEmail()) ||
                "".equals(loginVO.getPassword()) ||
                loginVO.getEmail() == null ||
                loginVO.getPassword() == null) {
            return ResponseMessage.successCodeMsgData(2001, "The email and password fields cannot be empty!", loginVO);
        }
        User user = userService.loginByEmail(loginVO.getEmail(), loginVO.getPassword());
        if (user == null) {
            return ResponseMessage.successCodeMsgData(2404, "User is not found or password is not validated!", loginVO);
        }
        loginVO.setToken(userAgentUtil.sign(user.getId()));
        return ResponseMessage.success(loginVO);
    }

    @DataLog
    @ApiOperation(value = "管理员登陆接口", tags = {"权限获取管理"})
    @PostMapping("/manager/login")
    @WebLog(hasToken = false)
    public ResponseMessage managerLogin(HttpServletResponse response, @RequestBody LoginVO loginVO) {
        if ("".equals(loginVO.getEmail()) ||
                "".equals(loginVO.getPassword()) ||
                loginVO.getEmail() == null ||
                loginVO.getPassword() == null) {
            return ResponseMessage.successCodeMsgData(2001, "The email and password fields cannot be empty!", loginVO);
        }
        User user = userService.loginByEmail(loginVO.getEmail(), loginVO.getPassword());
        if (user == null) {
            return ResponseMessage.successCodeMsgData(2404, "User is not found or password is not validated!", loginVO);
        }
        if (user.getRoleId() < RoleEnum.MANAGER.getCode()) {
            return ResponseMessage.successCodeMsgData(2500, "Insufficient permissions!", loginVO);
        }
        loginVO.setToken(userAgentUtil.sign(user.getId()));
        return ResponseMessage.success(loginVO);
    }

    @ApiOperation(value = "用户注册接口", tags = {"权限获取管理"})
    @PostMapping("/register")
    @DataLog
    public ResponseMessage register(@RequestBody RegisterVO registerVO) {
        if (registerVO == null || BeanUtil.hasNullField(registerVO)) {
            return ResponseMessage.errorMsg(2500, "User information cannot be null!", registerVO);
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
            return ResponseMessage.serverError(registerVO);
        }
    }

    @ApiOperation(value = "为用户发送邮件", tags = {"权限获取管理"})
    @PostMapping("/email")
    @DataLog
    @RequireRoles(RoleEnum.MANAGER)
    public ResponseMessage emailTo(@RequestBody EmailVO emailVO) {
        try {
            if (emailVO.getType() == 0) {
                EmailUtil.sendHtmlMail(emailVO.getEmail(), "账号审核已通过！", "恭喜您！账号申请已通过，功能已可正常使用！");
            } else if (emailVO.getType() == 1) {
                EmailUtil.sendHtmlMail(emailVO.getEmail(), "账号审核未通过！", emailVO.getMessage());
            } else if (emailVO.getType() == 2) {
                EmailUtil.sendCodeMail(emailVO.getEmail());
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
                EmailUtil.sendCodeMail(emailVO.getEmail());
                return ResponseMessage.success(emailVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.errorMsg(2500, e.getMessage(), emailVO);
        }
        return ResponseMessage.errorMsg(2500, "Mail type error！");
    }
}
