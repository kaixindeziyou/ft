package com.zrulin.ftcommunity.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 通过请求的信息查找凭证ticket，找到则返回，找不到则返回null
 * @author zrulin
 * @create 2022-03-13 9:22
 */
public class CookieUtil {
    public static String getValue(HttpServletRequest request , String name){
        if(request == null || name == null){
            throw new IllegalArgumentException("参数为空！");
        }
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies){
                if(cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
