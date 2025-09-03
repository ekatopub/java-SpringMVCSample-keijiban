package com.example.demo.aspect;


import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectExample {

    @After("execution(* com.example.demo.service.impl.*.*(..))")
    // @After("execution(* com.example.demo.service.impl.PrintServiceImplProduction.print(..))")
    // @After("execution(* com.example.demo.service.impl.PrintServiceImplTest.print(..) || execution(* com.example.demo.service.impl.PrintServiceImplProduction.print(..))")
    public void after() {
        System.out.println("serviceのメソッド終了");        
        
    }

}