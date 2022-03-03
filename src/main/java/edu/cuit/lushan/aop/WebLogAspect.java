package edu.cuit.lushan.aop;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.louislivi.fastdep.shirojwt.jwt.JwtUtil;
import edu.cuit.lushan.entity.SysLog;
import edu.cuit.lushan.service.ISysLogService;
import edu.cuit.lushan.utils.ResponseMessage;
import edu.cuit.lushan.utils.UserAgentUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    @Pointcut("execution(public * edu.cuit.lushan.controller.*.*(..))")
    public void logPointCut() {
    }

    //    @Before("logPointCut()")
    @AfterReturning(returning = "ret", pointcut = "logPointCut()")
    public void afterReturning(JoinPoint joinPoint, Object ret) throws Throwable {
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
        ResponseMessage responseMessage = ret == null?ResponseMessage.builder().build():(ResponseMessage) ret;

        String userId = userAgentUtil.getUserId(request)==null? null :userAgentUtil.getUserId(request).toString();
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
