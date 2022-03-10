package com.zrulin.ftcommunity;

import com.zrulin.ftcommunity.dao.DiscussPostMapper;
import com.zrulin.ftcommunity.pojo.DiscussPost;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zrulin
 * @create 2022-03-09 17:37
 */
@SpringBootTest
public class DiscussPostTest {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void test1(){
        for (DiscussPost discussPost : discussPostMapper.selectDiscussPost(0, 1, 3)) {
            System.out.println(discussPost);
        }

    }
    @Test
    public void test2(){
        Integer integer = discussPostMapper.selectDiscussPostRows(1);
        System.out.println(integer);
    }
}
