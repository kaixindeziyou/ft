package com.zrulin.ftcommunity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = FtCommunityApplication.class)
class FtCommunityApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("hello World!");
    }

}
