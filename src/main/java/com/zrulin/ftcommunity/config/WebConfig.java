package com.zrulin.ftcommunity.config;

import com.mysql.cj.log.Log;
import com.zrulin.ftcommunity.interceptor.DemoInterceptor;
import com.zrulin.ftcommunity.interceptor.LoginInterceptor;
import com.zrulin.ftcommunity.interceptor.LoginRequireInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author zrulin
 * @create 2022-03-13 8:40
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private DemoInterceptor demoInterceptor;

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private LoginRequireInterceptor loginRequireInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器，如果不做配置的话就会拦截一切请求
        // 路径 /**/*.css   第一个/指的时访问路径，http://localhost:8888/工程路径/
        // 也可以理解/**指的是static下面的所有文件。
        //excludePathPatterns() 不拦截的路径
        //addPathPatterns 拦截的路径。
        registry.addInterceptor(demoInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg")
                .addPathPatterns("/register","/login");

        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");

        registry.addInterceptor(loginRequireInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");
    }
}
