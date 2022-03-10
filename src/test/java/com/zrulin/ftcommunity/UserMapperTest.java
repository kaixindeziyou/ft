package com.zrulin.ftcommunity;

import com.zrulin.ftcommunity.dao.UserMapper;
import com.zrulin.ftcommunity.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * @author zrulin
 * @create 2022-03-09 18:20
 */
@SpringBootTest
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;
    @Test
    public void test(){
        System.out.println(userMapper.selectById(2));
        System.out.println(userMapper.selectByEmail("232452@qq.com"));
        System.out.println(userMapper.selectByUsername("zipper"));
    }
    @Test
    public void test1(){
        userMapper.insertUser(new User(null,"勇敢象","12341234","stgee","2353d32@111.com",0,1,null,null,new Date()));
    }
    @Test
    public void test2(){
        userMapper.updateHeader(1,"http://localhost:3323/shadiao");
        userMapper.updatePassword(1,"dongshan");
        userMapper.updateStatus(1,1);
    }
}
