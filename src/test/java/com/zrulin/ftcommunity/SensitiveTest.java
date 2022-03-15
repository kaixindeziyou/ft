package com.zrulin.ftcommunity;

import com.zrulin.ftcommunity.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zrulin
 * @create 2022-03-14 14:57
 */
@SpringBootTest
public class SensitiveTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Test
    public void test(){
        String test = "票始*……*&&博访问啦 开，票但是哈哈哈";
        String result = sensitiveFilter.filter(test);
        System.out.println(result);
    }
}
