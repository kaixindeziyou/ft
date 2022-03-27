package com.zrulin.ftcommunity.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zrulin
 * @create 2022-03-18 18:28
 */
@Aspect
@Component
public class ServiceLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Pointcut("execution(* com.zrulin.ftcommunity.service.*.*(..))")
    public void pointcut(){
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint){//除了环绕通知以外的通知也可以加这个连接点参数
        // 用户[1,2,3,4] 在 [xxx],访问了[com.zrulin.ftcommunity.service.xxx()]
        // 要记录这样格式的日志，首先用户的ip可以用request获取，在这个里面获取request不能简单的参数里面获取，用一个工具类RequestContextHolder、
        //返回的默认类型是RequestAttributes，这里把他转为子类型ServletRequestAttributes，功能多一点
         ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
         if(attributes == null){
             return;//后面增加的kafka消费者调用service层，而不是controller调用，不是请求,获取不到request
         }
         HttpServletRequest request = attributes.getRequest();//获得request对象
         String ip = request.getRemoteHost();//获得用户ip
         String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
         //获得[com.zrulin.ftcommunity.service.xxx()],前面是。。。后面是获得方法名
         String target = joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
         logger.info(String.format("[用户【%s】,在【%s】,访问了【%s】。]",ip,now,target));
    }
}
