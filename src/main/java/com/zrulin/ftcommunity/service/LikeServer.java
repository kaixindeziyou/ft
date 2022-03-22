package com.zrulin.ftcommunity.service;

/**
 * @author zrulin
 * @create 2022-03-22 17:09
 */
public interface LikeServer {

    /**
     * 点赞
     * @param UserId
     * @param EntityType
     * @param EntityId
     */
    public void like(int userId, int entityType, int entityId);

    /**
     * 查询点赞的数量
     * @param entityType
     * @param entityId
     * @return
     */
    public long findEntityLikeCount(int entityType, int entityId);

    /**
     * 查询用户点赞的状态
     * 1-已赞，0-未赞
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public int findUserLikeStatus(int userId, int entityType, int entityId);
}
