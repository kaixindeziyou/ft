package com.zrulin.ftcommunity.contorller.advice;

import com.zrulin.ftcommunity.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author zrulin
 * @create 2022-03-18 16:22
 */
//@ControllerAdvice 这个组件会扫描所有的bean,annotations = Controller.class只扫描带有Controller的注解
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    //加一个方法，用来处理所有的错误情况
    // @ExceptionHandler()括号里写要声明哪些异常
    //Exception是所有异常的父类，所有异常都用它来处理就完了。
    //方法必须是公有的，没返回值。
    //方法中可以带很多参数，常用的就三个：
        //Exception : controller中带的异常，发生异常就会把这个异常传过来，我们可以处理这个异常。
        //request 和 response
    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletResponse response, HttpServletRequest request ) throws IOException {
        //把异常记录到日志里面
        logger.error("服务器发生异常："+e.getMessage());
        //记录详细异常信息
        for(StackTraceElement element : e.getStackTrace()){
            logger.error(element.toString());
        }
        //判断发生的请求是普通请求，还是异步请求（返回json的那种）
        String xRequestedWith = request.getHeader("x-requested-with");
        if("XMLHttpRequest".equals(xRequestedWith)){//说明是异步请求
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJsonString(1,"服务器异常！"));
        }else {
            response.sendRedirect(request.getContextPath() +"/error");
        }
    }
}
