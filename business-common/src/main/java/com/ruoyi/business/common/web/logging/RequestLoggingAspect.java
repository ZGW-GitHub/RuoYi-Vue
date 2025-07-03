package com.ruoyi.business.common.web.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Snow
 */
@Slf4j
@Aspect
@Component
public class RequestLoggingAspect {

    @Autowired
    private ObjectMapper objectMapper;

    @Pointcut("@annotation(com.ruoyi.business.common.web.logging.RequestLogging) || @within(com.ruoyi.business.common.web.logging.RequestLogging)")
    public void requestLoggingPointcut() {}

    @Around("requestLoggingPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        log.debug("【 RequestLoggingAspect 】参数: {}", objectMapper.writeValueAsString(args));

        Object result = joinPoint.proceed();
        log.debug("【 RequestLoggingAspect 】返回值: {}", result);

        return result;
    }

}
