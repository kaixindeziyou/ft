package com.zrulin.ftcommunity.service;

import java.util.List;
import java.util.Map;

/**
 * @author zrulin
 * @create 2022-03-23 10:13
 */
public interface FollowService {


    /**
     * 关注功能
     * redis增加两条信息
     * 第一条，用户关注的实体set(目前有三种可能，关注的用户，关注的帖子，关注的评论)
     * 第二条，实体获得的关注用户set
     * @param userId
     * @param entityType
     * @param entityId
     */
    public void follow(int userId, int entityType, int entityId);

    /**
     * 取消关注功能
     * redis删除两条信息
     * 第一条，用户关注的实体set(目前有三种可能，关注的用户，关注的帖子，关注的评论)
     * 第二条，实体获得的关注用户set
     * @param userId
     * @param entityType
     * @param entityId
     */
    public void unfollow(int userId, int entityType, int entityId);

    /**
     * 获得关注某种实体的数量，
     * @param userId
     * @param entityType
     * @return
     */
    public long findFolloweeCount(int userId, int entityType);

    /**
     * 获得实体的粉丝数量
     * @param entityType
     * @param entityId
     * @return
     */
    public long findFollowerCount(int entityType, int entityId);

    /**
     * 查询当前用户是否关注了该实体
     * @param entityType  该实体的类型
     * @param entityId    该实体的id
     * @param userId      当前用户的id
     * @return
     */
    public boolean followStatus(int entityType, int entityId, int userId);

    /**
     * 查找关注的，某个实体（用户，帖子，评论中的一种）列表（id 以及 时间轴，也就是zset中的分数）
     * 支持 分页
     * @param userId
     * @param entityType
     * @param offset
     * @param limit
     * @return
     */
    public List<Map<String,Object>> findFollowees(int userId, int entityType, int offset, int limit);

    /**
     * 查找关注这个实体的粉丝列表。
     * @param entityType
     * @param entityId
     * @param offset
     * @param limit
     * @return
     */
    public List<Map<String,Object>> findFollowers(int entityType, int entityId, int offset, int limit);
}
