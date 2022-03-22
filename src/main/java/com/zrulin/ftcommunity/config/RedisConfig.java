package com.zrulin.ftcommunity.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author zrulin
 * @create 2022-03-21 23:26
 */
@Configuration
public class RedisConfig {

    //new一个对象（redisTemplate对象）
    //其实Spring boot会自动配置一个redisTemplate对象，但是它的key是Object类型的，我们为了使用String更加方便，重新配置。
//    参数RedisConnectionFactory，这个bean已经被容器装配了，直接注入进来。
    @Bean
    public RedisTemplate<String,Object> redisTemplate (RedisConnectionFactory factory){
        //设置它的相关的格式
        //实例化这个Bean
        RedisTemplate<String,Object> template = new RedisTemplate<>();
        //设置这个连接工厂，具备访问redis数据库的能力
        template.setConnectionFactory(factory);

        //配置主要是配序列化的方式，我们写的程序是Java程序，得到Java类型的数据，最终要把这个数据存到redis数据库中
        //就要指定一种序列化的方式，或者说是数据转化的方式

        //设置key的序列化方式
        //RedisSerializer.string()返回一个能够序列化字符串的序列化器
        template.setKeySerializer(RedisSerializer.string());
        //设置value的序列化方式
        template.setValueSerializer(RedisSerializer.json());
        //设置hash的key的序列化方式
        template.setHashKeySerializer(RedisSerializer.string());
        //设置hash的value的序列化方式
        template.setHashValueSerializer(RedisSerializer.json());

        //为了让Template中的这些参数生效，要触发一下
        template.afterPropertiesSet();
        return template;
    }
}
