package com.zrulin.ftcommunity.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 验证码配置类
 * 设置验证码code和image的参数，实例化bean交给Spring容器管理，返回一个Producer对象。
 * @author zrulin
 * @create 2022-03-12 8:29
 */
@Configuration
public class KaptchaConfig {
    //这个Bean将会被Spring容器所管理，所装配，我们要装配的肯定是这个工具的核心的代码，对象。
    //Kaptcha核心的对象是一个接口，Producer
    @Bean
    public Producer kaptchaProducer(){
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width","100");//单位是像素。
        properties.setProperty("kaptcha.image.hight","40");//单位是像素。
        properties.setProperty("kaptcha.textproducer.font.size","32");//字号大小。
        properties.setProperty("kaptcha.textproducer.font.color","0,0,0");//0,0,0表示黑色，直接写单词也可以。
        properties.setProperty("kaptcha.textproducer.char.string","0123456789ABCDEFGHIJKLNMOPQRSTUVWXYZ");//随机字母的范围
        properties.setProperty("kaptcha.textproducer.char.length","4");//随机字母的长度限定
        //要采用哪个干扰类，干扰类的作用就是图片上的点，线，阴影等干扰元素。默认干扰就做的很好所以。
        properties.setProperty("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");


        //DefaultKaptcha 是Producer接口默认的实现类。
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        //传入配置，参数，到一个Config对象里面。
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
