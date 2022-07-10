package com.zrulin.ftcommunity.service;

import com.zrulin.ftcommunity.pojo.DiscussPost;

import java.util.List;
import java.util.Map;

/**
 * @author zrulin
 * @create 2022-07-09 15:04
 */
public interface ElasticsearchService {

    public void saveDiscussPost(DiscussPost discussPost);

    public void deleteDiscussPost(int id);

    public Map<String ,Object> searchDiscussPost(String keyword, int current, int limit);
}
