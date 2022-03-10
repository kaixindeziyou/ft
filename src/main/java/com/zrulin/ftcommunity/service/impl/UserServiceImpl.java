package com.zrulin.ftcommunity.service.impl;

import com.zrulin.ftcommunity.dao.UserMapper;
import com.zrulin.ftcommunity.pojo.User;
import com.zrulin.ftcommunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zrulin
 * @create 2022-03-09 18:53
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User findUserById(Integer id) {
        return userMapper.selectById(id);
    }
}
