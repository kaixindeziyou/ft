package com.zrulin.ftcommunity.service.impl;

import com.zrulin.ftcommunity.dao.DiscussPostMapper;
import com.zrulin.ftcommunity.dao.UserMapper;
import com.zrulin.ftcommunity.pojo.DiscussPost;
import com.zrulin.ftcommunity.pojo.User;
import com.zrulin.ftcommunity.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;

/**
 * 做事务管理的Demo测试
 * @author zrulin
 * @create 2022-03-15 17:03
 */
@Service
public class DemoService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;


    /**
     * 通过事务的方式管理事务，如果任何地方出现错误都要回滚回去
     * Isolation.READ_COMMITTED：隔离级别，读取已提交的数据
     * propagation：事务的传播机制。用来处理这类问题：（业务方法A调用业务方法B假设这两个事务都有事务管理，则应该以谁的为准）
     * REQUIRE：支持当前事务（A调用B,A就是当前事务。也可以说是外部事物，对B来说。）如果当前事务没有事务管理就创建新事务。
     * REQUIRES_NEW：创建新的事务，并且暂停当前事务
     * NESTED：如果当前存在事务（外部事务），则嵌套在该事务中执行。（独立的提交和回滚），如果外部事务不存在，创建新事务。
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public Object save1(){
        //新增用户
        User user = new User();
        user.setUsername("affair");
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getSalt()+"123"));
        user.setEmail("f34fljso@163.com");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //新增帖子
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle("hello");
        post.setContent("新人报道");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);

        Integer.valueOf("abc");
        return "ok";
    }

    public Object save2(){
        //设置隔离级别
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        //设置传播机制
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        return transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                //新增用户
                User user = new User();
                user.setUsername("affair1");
                user.setSalt(CommunityUtil.generateUUID().substring(0,5));
                user.setPassword(CommunityUtil.md5(user.getSalt()+"123"));
                user.setEmail("ffljso@163.com");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);

                //新增帖子
                DiscussPost post = new DiscussPost();
                post.setUserId(user.getId());
                post.setTitle("你好");
                post.setContent("新住报道");
                post.setCreateTime(new Date());
                discussPostMapper.insertDiscussPost(post);

                Integer.valueOf("abc");
                return "ok";
            }
        });
    }
}
