package com.example.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    /**
     * DBアクセス発生のログを出力するアスペクト
     *
     * ポイントカット: com.example.demo.service.impl.UserDetailsServiceImpl.loadUserByUsername()
     */
    @Before("execution(* com.example.demo.service.impl.UserDetailsServiceImpl.loadUserByUsername(..))")
    public void logDbAccess(JoinPoint joinPoint) {
        System.out.println("DBアクセス発生: " + joinPoint.getSignature().toShortString());
    }
}