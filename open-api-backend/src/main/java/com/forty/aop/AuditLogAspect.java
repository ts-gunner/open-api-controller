package com.forty.aop;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.MessageFormat;


/**
 * 已在网关中实现该方法， 无需在微服务中重复实现
 */
//@Component
@Slf4j
//@Aspect
public class AuditLogAspect {

    @Pointcut("execution(* com.forty.controller.*.*(..))")
    public void pointcut() {}

//    @Before("pointcut()")
//    public void beforeInterceptor(JoinPoint joinPoint) {
//        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
//        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
//        StringBuffer requestURL = request.getRequestURL();
//        log.info(MessageFormat.format("calling {0}",requestURL));
//    }
}
