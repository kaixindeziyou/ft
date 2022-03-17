package com.zrulin.ftcommunity;

import com.zrulin.ftcommunity.service.impl.DemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zrulin
 * @create 2022-03-15 17:21
 */
@SpringBootTest
public class TransactionTest {

    @Autowired
    private DemoService demoService;

    @Test
    public void test(){
//        demoService.save1();
    }
    @Test
    public void test1(){
        Object o = demoService.save2();
        System.out.println(o);
    }
}
