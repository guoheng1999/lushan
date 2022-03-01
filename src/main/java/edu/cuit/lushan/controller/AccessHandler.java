package edu.cuit.lushan.controller;

import cn.hutool.core.bean.BeanUtil;
import edu.cuit.lushan.annotation.DataLog;
import edu.cuit.lushan.entity.User;
import edu.cuit.lushan.enums.UserVOEnum;
import edu.cuit.lushan.factory.AbstractFactory;
import edu.cuit.lushan.factory.FactoryProducer;
import edu.cuit.lushan.service.IUserService;
import edu.cuit.lushan.utils.ResponseMessage;
import edu.cuit.lushan.utils.UserAgentUtil;
import edu.cuit.lushan.vo.LoginVO;
import edu.cuit.lushan.vo.RegisterVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@ApiModel(value = "用户登陆注册中心", description = "用户登录")
public class AccessHandler {
    @Autowired
    IUserService userService;
    @Autowired
    UserAgentUtil userAgentUtil;

    AbstractFactory<User> abstractFactory = FactoryProducer.getFactory(FactoryProducer.FactoryName.USER);
    @DataLog
    @ApiOperation(value = "用户登录接口", tags = {"权限获取管理"})
    @PostMapping("/login")
    public ResponseMessage login(HttpServletResponse response,@RequestBody LoginVO loginVO) {
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
        response.addHeader("token", userAgentUtil.sign(user.getId()));
        return ResponseMessage.success(abstractFactory.buildVOByEntity(user, UserVOEnum.USER_INFO.name()));
    }

    @ApiOperation(value = "用户注册接口", tags = {"权限获取管理"})
    @PostMapping("/register")
    @DataLog
    public ResponseMessage register(@RequestBody RegisterVO registerVO) {
        if (registerVO == null || BeanUtil.hasNullField(registerVO)){
            return ResponseMessage.errorMsg(2500, "User information cannot be null!", registerVO);
        }
        AbstractFactory<User> abstractFactory = FactoryProducer.getFactory(FactoryProducer.FactoryName.USER);
        User user = abstractFactory.buildEntityByVO(new User(), registerVO);
        userService.save(user);
        user = userService.selectByEmail(user.getEmail());
        user.setModifyUserId(user.getId());
        userService.updateById(user);
        return ResponseMessage.success(user);
    }
}
