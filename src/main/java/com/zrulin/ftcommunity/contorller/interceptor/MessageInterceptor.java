package com.zrulin.ftcommunity.contorller.interceptor;

import com.zrulin.ftcommunity.pojo.User;
import com.zrulin.ftcommunity.service.MessageService;
import com.zrulin.ftcommunity.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zrulin
 * @create 2022-03-28 17:21
 */
@Component
public class MessageInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user != null && modelAndView != null){
            int count = messageService.findAllUnread(user.getId());
            modelAndView.addObject("AllUnreadCount",count);
        }
    }
}
