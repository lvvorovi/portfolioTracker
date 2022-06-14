package com.portfolioTracker.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@EnableAspectJAutoProxy
public class LoggingAspect {

    @Before("execution(public * com.portfolioTracker.payload..*(..))")
    public void log(JoinPoint joinPoint) {
        log.info("Request id {} is calling a public method {} in payload package",
                MDC.get("request_id"),
                joinPoint.toShortString());
    }
}
