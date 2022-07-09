package com.zrulin.ftcommunity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class FtCommunityApplication {
    //管理bean的生命周期的，主要是用来管理bean初始化的方法。由这个注解所修饰的方法，会在构造器调用完以后被执行。
    //所以通常是初始化方法
    @PostConstruct
    public void init(){
        //解决netty启动冲突的问题
        //可以去看 Netty4Utils类中的setAvailableprocessors()方法
        System.setProperty("es.set.netty.runtime.available.processors","false");
    }

    public static void main(String[] args) {
        SpringApplication.run(FtCommunityApplication.class, args);
    }

}
