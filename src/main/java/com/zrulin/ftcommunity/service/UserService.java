package com.zrulin.ftcommunity.service;

import com.zrulin.ftcommunity.pojo.User;

/**
 * @author zrulin
 * @create 2022-03-09 18:53
 */
public interface UserService {

    public User findUserById(Integer id);
}
