package com.zrulin.ftcommunity.contorller.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zrulin
 * @create 2022-03-13 8:02
 */
@Component
public class DemoInterceptor implements HandlerInterceptor {
    //首先声明@Component 交给Spring容器去管理
    //实现HandlerInterceptor，拦截器的接口
    //接口里面有preHandle，postHandle，afterCompletion，三种方法，但是都是default类型的，默认实现的，所以想实现哪个就实现哪个就好。
    // Ogject handler 是拦截的目标 比如说拦截路径 /login  那这个就是/login对应的方法。

    private static final Logger logger = LoggerFactory.getLogger(DemoInterceptor.class);

    //在Controller之前执行。
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        logger.debug("preHandle："+ handler.toString());
        //如果return false  方法就不会往下执行，Cotroller就不会被执行。
        return true;
    }

    //在调用完Controller之后执行的
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        logger.debug("postHandle:"+handler.toString());
    }

    //在模板引擎（TemplateEngine）执行之后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        logger.debug("afterCompletion:"+handler.toString());
    }
}
