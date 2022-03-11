package com.zrulin.ftcommunity.service;

import com.zrulin.ftcommunity.pojo.User;

import java.util.Map;

/**
 * @author zrulin
 * @create 2022-03-09 18:53
 */
public interface UserService {

    public User findUserById(Integer id);

    /**
     * 判断合法性，注册，发送邮件
     * @param user
     * @return
     */
    public Map<String,Object> register(User user);

    /**
     * 激活账户
     * @param userId
     * @param code
     * @return
     */
    public Integer activation(Integer userId,String code);
}
