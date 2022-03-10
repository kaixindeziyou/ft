package com.zrulin.ftcommunity.service;

import com.zrulin.ftcommunity.pojo.DiscussPost;

import java.util.List;

/**
 * @author zrulin
 * @create 2022-03-09 17:43
 */
public interface DiscussPostService {

    /**
     * 按照条件查询帖子信息
     * @param userId 数值为零则查所有，非零则查询指定userId的帖子
     * @param offset 起始页
     * @param limit 每页数量
     * @return
     */
    public List<DiscussPost> findDiscussPosts(int userId, int offset ,int limit);

    /**
     * 查询帖子数量
     * @param userId 数值为零则查所有，非零则查询指定userId的帖子数量
     * @return
     */
    public int findDiscussPostRows(int userId);
}
