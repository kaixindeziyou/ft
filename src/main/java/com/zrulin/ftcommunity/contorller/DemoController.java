package com.zrulin.ftcommunity.contorller;

import com.zrulin.ftcommunity.util.CommunityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author zrulin
 * @create 2022-03-11 15:38
 */
@Controller
public class DemoController {
    /**
     * cookie 示例
     * @param response
     * @return
     */
    @GetMapping("/cookie/set")
    @ResponseBody
    public String SetCookie(HttpServletResponse response){
        //创建cookie
        //cookie没有无参构造器，参数必须都是字符串，并且每个cookie只能存一组字符串，
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        //设置Cookie生效的范围,有些路径的访问不必要携带cookie。
        cookie.setPath("/ft");
        //设置cookie的生存时间，这个方法的单位是秒
        cookie.setMaxAge(60*10);
        //发送cookie
        response.addCookie(cookie);
        return "set Cookie";
    }
    @GetMapping("/cookie/get")
    @ResponseBody
    public String GetCookie(@CookieValue("code") String code){
        System.out.println(code);
        return "get Cookie";
    }

    /**
     * session示例
     * session中存放什么数据都行，因为session一直在服务端保存着。
     * 但是cookie中只能存字符串，而且只能存少量数据，因为cookie一直在客户端和服务器之间来回传送。
     * cookie信息不能什么都写，安全问题。数据不能太多，性能问题。
     * @param session
     * @return
     */
    @GetMapping("/session/set")
    @ResponseBody
    public String SetSession(HttpSession session){
        session.setAttribute("name","张三");
        session.setAttribute("age",88);
        return "session set";

    }
}
