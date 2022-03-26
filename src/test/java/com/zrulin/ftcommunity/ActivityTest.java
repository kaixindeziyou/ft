package com.zrulin.ftcommunity;

import com.zrulin.ftcommunity.dao.ActivityMapper;
import com.zrulin.ftcommunity.pojo.Activity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * @author zrulin
 * @create 2022-03-25 16:52
 */
@SpringBootTest
public class ActivityTest {

    @Autowired
    private ActivityMapper activityMapper;

    @Test
    public void test(){
        for (Activity activity : activityMapper.selectActivity(0, 3)) {
            System.out.println(activity);
        }
        System.out.println(activityMapper.selectActivityRows());
    }
    @Test
    public void test1(){
        Activity activity = activityMapper.selectActivityById(1);
        System.out.println(activity);
    }

    @Test
    public void test3(){
        Activity activity = new Activity();
        activity.setIssuer("test");
        activity.setStartTime(new Date());
        activity.setEndTime(new Date());
        activity.setTitle("test");
        activity.setContent("test");
        activity.setCreateTime(new Date());
        activity.setPhoto(null);
        activity.setEmail("djwoje");
        activity.setStatus(0);
        activity.setCommentCount(0);
        int i = activityMapper.insertActivity(activity);
        System.out.println(i);
    }

}
