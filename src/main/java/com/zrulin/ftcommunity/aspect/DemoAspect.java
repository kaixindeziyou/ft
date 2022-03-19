package com.zrulin.ftcommunity.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author zrulin
 * @create 2022-03-18 17:49
 */
//@Component
//@Aspect
public class DemoAspect {
    @Pointcut("execution(* com.zrulin.ftcommunity.service.*.*(..))")
    public void pointcut(){
    }

    @Before("pointcut()")
    public void before(){
        System.out.println("before");
    }

    @After("pointcut()")
    public void after(){
        System.out.println("after");
    }

    @AfterReturning("pointcut()")
    public void afterReturning(){
        System.out.println("afterReturning");
    }

    @AfterThrowing("pointcut()")
    public void afterThrowing(){
        System.out.println("afterThrowing");
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {//这个参数是连接点
        System.out.println("around  before");
        //Object 是如果目标组件有返回值
        Object obj = joinPoint.proceed();//调目标对象被处理的那个逻辑(就是目标组件的方法)
        System.out.println("around  after");
        return obj;
    }
}
