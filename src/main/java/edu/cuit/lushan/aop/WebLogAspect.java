package edu.cuit.lushan.aop;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import edu.cuit.lushan.annotation.WebLog;
import edu.cuit.lushan.entity.SysLog;
import edu.cuit.lushan.exception.AuthorizationException;
import edu.cuit.lushan.service.ISysLogService;
import edu.cuit.lushan.utils.ResponseMessage;
import edu.cuit.lushan.utils.UserAgentUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

@Aspect
@Component
@Slf4j
public class WebLogAspect {
    @Autowired
    UserAgentUtil userAgentUtil;
    @Autowired
    ISysLogService sysLogService;

//    @Pointcut("execution(public * edu.cuit.lushan.controller.*.*(..))")

    @PostConstruct
    public void logPointCut() {
    }


    @AfterReturning(returning = "ret", pointcut = "@annotation(webLog)")
    public void afterReturning(JoinPoint joinPoint, Object ret, WebLog webLog) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        String requestURL = request.getRequestURL().toString();
        String httpMethod = request.getMethod();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "."
                + joinPoint.getSignature().getName();
        String param = Arrays.toString(joinPoint.getArgs());
        String ip = getIP(request);
        ResponseMessage responseMessage = ret == null ? ResponseMessage.builder().build() : (ResponseMessage) ret;
        // 判断请求是否携带token
        System.err.println(classMethod);
        String token = userAgentUtil.getToken(request);
        System.err.println(webLog.hasToken());
        if ((StrUtil.isEmpty(token)|| StrUtil.isBlank(token) )&& webLog.hasToken()) {
            System.err.println("token= " + token);
            throw new AuthorizationException("This request need a token!");
        }
        if ("null".equals(token)) throw new AuthorizationException("请不要乱搞！");
        String userId = userAgentUtil.getUserId(request) == null ? null : userAgentUtil.getUserId(request).toString();
        SysLog sysLog = SysLog.builder().ip(ip)
                .methodName(classMethod)
                .requestParam(param)
                .requestTime(LocalDateTimeUtil.of(new Date()))
                .responseData(responseMessage.toString())
                .responseCode(responseMessage.getCode())
                .userId(userId)
                .httpMethod(httpMethod)
                .requestUrl(requestURL)
                .build();
        log.info(sysLog.toString());
        sysLogService.save(sysLog);
    }

    private static String getIP(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
