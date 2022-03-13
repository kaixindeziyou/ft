package com.zrulin.ftcommunity.contorller;

import com.google.code.kaptcha.Producer;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.zrulin.ftcommunity.config.KaptchaConfig;
import com.zrulin.ftcommunity.pojo.User;
import com.zrulin.ftcommunity.service.UserService;
import com.zrulin.ftcommunity.service.impl.UserServiceImpl;
import com.zrulin.ftcommunity.util.CommunityConstant;
import com.zrulin.ftcommunity.util.CommunityUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * 登录注册功能controller
 * @author zrulin
 * @create 2022-03-10 21:24
 */
@Controller
public class LoginController implements CommunityConstant {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private Producer kaptchaProduce;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Value("${server.servlet.context-path}")
    private String contextPath;

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
        }else if(activation == ACTIVATION_REPEAT){
            model.addAttribute("msg","无效操作，该账户已激活！");
            model.addAttribute("target","/index");
        }else{
            model.addAttribute("msg","激活失败，您提供的激活码不正确！");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }

    /**
     * 生成验证码
     * 通过Produce对象生成Text文本和Image图片。
     * 将文本存入session对象，便于登录的时候进行验证码校验
     * 将图片通过response中的输出流，将图片输出给浏览器。
     * @param session
     * @param response
     */
    @GetMapping("/kaptcha")
    public void getKaptcha(HttpSession session, HttpServletResponse response){
        //生成验证码
        String text = kaptchaProduce.createText();
        //生成图片
        BufferedImage image = kaptchaProduce.createImage(text);

        //验证码存入session，好在后面的请求中验证验证码是否正确
        session.setAttribute("code",text);
        //将图片输出给浏览器，人工的输出。
        response.setContentType("image/png");//声明浏览器返回什么样的数据

        try {
            //获取response的输出流，这个输出流整个由springMVC维护管理
            ServletOutputStream outputStream = response.getOutputStream();
            //向浏览器输出图片的工具。(图片，格式，使用的输出流)
            ImageIO.write(image,"png",outputStream);
        } catch (IOException e) {
            logger.error("响应验证码失败："+e.getMessage());
        }
    }

    /**
     * 登录功能实现
     * 检查验证码，账号密码，生成ticket凭证，response加入cookie
     * 在mysql数据库中包存凭证信息。
     * @param model
     * @param username
     * @param password
     * @param code
     * @param rememberme
     * @param session
     * @param response
     * @return
     */
    @PostMapping("/login")
    public String login(Model model,
                        String username,
                        String password,
                        String code,
                        Boolean rememberme,
                        HttpSession session,
                        HttpServletResponse response
                         ){
        //检查验证码
        String kaptcha = (String) session.getAttribute("code");
        if(StringUtils.isBlank(code) || StringUtils.isBlank(kaptcha) || !kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg","验证码不正确");
            return "/site/login";
        }
        //检查账号密码
        int expiredSeconds = rememberme!=null?REMEMBER_EXPIRED_SECONDS:DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if(map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        }else{
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/login";
        }
    }

    /**
     * 退出登录
     * 将凭证信息中的status改为失效
     * @param ticket
     * @return
     */
    @GetMapping("/logout")
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
       return "redirect:/login";
    }
}
