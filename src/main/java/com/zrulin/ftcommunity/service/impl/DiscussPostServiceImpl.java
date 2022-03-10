package com.zrulin.ftcommunity.service.impl;

import com.zrulin.ftcommunity.dao.DiscussPostMapper;
import com.zrulin.ftcommunity.pojo.DiscussPost;
import com.zrulin.ftcommunity.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * @author zrulin
 * @create 2022-03-09 17:43
 */
@Service
public class DiscussPostServiceImpl implements DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Override
    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPost(userId,offset,limit);
    }

    @Override
    public int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
