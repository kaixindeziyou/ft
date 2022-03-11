package com.zrulin.ftcommunity.contorller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.zrulin.ftcommunity.pojo.User;
import com.zrulin.ftcommunity.service.UserService;
import com.zrulin.ftcommunity.service.impl.UserServiceImpl;
import com.zrulin.ftcommunity.util.CommunityConstant;
import com.zrulin.ftcommunity.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author zrulin
 * @create 2022-03-10 21:24
 */
@Controller
public class LoginController implements CommunityConstant {
    @Autowired
    private UserServiceImpl userService;

    /**
     * 定向为注册页面
     * @return
     */
    @GetMapping("/register")
    public String getRegisterPage(){
        return "/site/register";
    }

    /**
     * 定向为登录页面
     * @return
     */
    @GetMapping("/login")
    public String getLoginPage(){
        return "/site/login";
    }

    /**
     * 接受数据，注册功能实现
     * @param model
     * @param user
     * @return
     */
    @PostMapping("/register")
    public String register(Model model, User user){
        Map<String, Object> map = userService.register(user);
        if(map == null || map.isEmpty()){
            model.addAttribute("msg","注册成功，我们已经向您的邮箱发送了一封激活邮件，请尽快激活！");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        }else{
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/site/register";
        }
    }

    /**
     * 激活功能实现，邮箱激活。
     * @param model
     * @param userId
     * @param code
     * @return
     */
    @GetMapping("/activation/{userId}/{code}")
    public String activation(Model model,
                             @PathVariable("userId") int userId,
                             @PathVariable("code") String code){
        Integer activation = userService.activation(userId, code);
        //最好不要手写这些常量，而是调用接口。（复用。抽象。？）
        if(activation == ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功，您的账户已经可以正常使用了！");
            model.addAttribute("target","/login");
        }else if(activation == ACTIVATION_FAILURE){
            model.addAttribute("msg","无效操作，该账户已激活！");
            model.addAttribute("target","/index");
        }else{
            model.addAttribute("msg","激活失败，您提供的激活码不正确！");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }


}
