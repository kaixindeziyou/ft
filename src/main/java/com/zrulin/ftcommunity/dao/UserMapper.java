package com.zrulin.ftcommunity.dao;

import com.zrulin.ftcommunity.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zrulin
 * @create 2022-03-09 18:11
 */
@Mapper
public interface UserMapper {

    public User selectById(int id);

    public User selectByUsername(String username);

    public User selectByEmail(String email);

    public Integer insertUser(User user);

    public Integer updateStatus(int id,int status);

    public Integer updateHeader(int id,String headerUrl);

    public Integer updatePassword(int id,String password);
}
