package com.zrulin.ftcommunity.service.impl;

import com.zrulin.ftcommunity.service.LikeServer;
import com.zrulin.ftcommunity.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * @author zrulin
 * @create 2022-03-22 17:11
 */
@Service
public class LikeServiceImpl implements LikeServer {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 在实现的时候，有两个维度的操作，一个是点赞，在like:entity:entityType:entityId中加入一个或者取消一个userId（点赞的用户的Id）
     * 同时需要在like:user:userId（被点赞的用户的Id）中加一个点赞的数量或者减少一个点赞的数量
     * 要保证事务性，这两个操作要么全部执行，要么全部不执行，不可被打断。
     *
     * 事务是一个单独的隔离操作：事务中的所有命令都会序列化、按顺序地执行。事务在执行的过程中，不会被其他客户端发送来的命令请求所打断。
     * 事务是一个原子操作：事务中的命令要么全部被执行，要么全部都不执行。
     * @param userId
     * @param entityType
     * @param entityId
     * @param entityUserId  这个是实体所属的用户的id，这个信息从数据库查就降低性能了，所以采取从前端传入参数的方式。
     */
    @Override
    public void like(int userId, int entityType, int entityId ,int entityUserId) {
//        String getLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
//        boolean isMember = redisTemplate.opsForSet().isMember(getLikeKey, userId);
//        if(isMember){
//            redisTemplate.opsForSet().remove(getLikeKey, userId);
//        }else{
//            redisTemplate.opsForSet().add(getLikeKey, userId);
//        }
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String getLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userKey = RedisKeyUtil.getUserKey(entityUserId);
                //这个查询操作一定要放到事务之外，如果事务之内不会立即得到结果。
                // 因为他在事务当中执行的所有命令没有立刻执行，而是把这些命令放到了队列里面，
                //当你提交事务的时候统一提交，在执行。

                Boolean isMember = operations.opsForSet().isMember(getLikeKey, userId);
                if(isMember){
                    operations.opsForSet().remove(getLikeKey, userId);
                    operations.opsForValue().decrement(userKey);
                }else{
                    operations.opsForSet().add(getLikeKey,userId);
                    operations.opsForValue().increment(userKey);
                }
                return null;
            }
        });
    }

    @Override
    public long findEntityLikeCount(int entityType, int entityId) {
        String getLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(getLikeKey);
    }

    @Override
    public int findUserLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(likeKey,userId)?1:0;
    }

    @Override
    public int findUserLikeCount(int userId) {
        String userKey = RedisKeyUtil.getUserKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userKey);
        return count == null ? 0 : count.intValue();
    }
}
