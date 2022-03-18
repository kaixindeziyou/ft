package com.zrulin.ftcommunity.contorller.interceptor;

import com.zrulin.ftcommunity.pojo.LoginTicket;
import com.zrulin.ftcommunity.pojo.User;
import com.zrulin.ftcommunity.service.impl.UserServiceImpl;
import com.zrulin.ftcommunity.util.CookieUtil;
import com.zrulin.ftcommunity.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author zrulin
 * @create 2022-03-13 9:13
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = CookieUtil.getValue(request, "ticket");
        if(ticket != null){
            //查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            //检查凭证是否有效
            if(loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())){
                //根据凭证查询用户
                User user = userService.findUserById(loginTicket.getUserId());
                //在本次请求中持有用户。考虑这个持有的时候要考虑到多线程的情况，每个请求对应一个线程，每个线程单独存一份，不会互相干扰。
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //在controller之后将user对象装入model
        User user = hostHolder.getUser();
        if(user != null && modelAndView != null){
            modelAndView.addObject("loginUser",user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //在展示完页面之后将这个user清空，节省内存。
        hostHolder.clear();
    }
}
