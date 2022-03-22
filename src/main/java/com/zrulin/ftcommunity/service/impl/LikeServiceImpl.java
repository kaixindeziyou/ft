package com.zrulin.ftcommunity.service.impl;

import com.zrulin.ftcommunity.service.LikeServer;
import com.zrulin.ftcommunity.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author zrulin
 * @create 2022-03-22 17:11
 */
@Service
public class LikeServiceImpl implements LikeServer {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void like(int userId, int entityType, int entityId) {
        String getLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        boolean isMember = redisTemplate.opsForSet().isMember(getLikeKey, userId);
        if(isMember){
            redisTemplate.opsForSet().remove(getLikeKey, userId);
        }else{
            redisTemplate.opsForSet().add(getLikeKey, userId);
        }

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

}
