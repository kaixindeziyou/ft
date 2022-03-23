package com.zrulin.ftcommunity.service.impl;

import com.zrulin.ftcommunity.pojo.User;
import com.zrulin.ftcommunity.service.FollowService;
import com.zrulin.ftcommunity.service.UserService;
import com.zrulin.ftcommunity.util.RedisKeyUtil;
import io.lettuce.core.models.role.RedisUpstreamInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author zrulin
 * @create 2022-03-23 10:20
 */
@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Override
    public void follow(int userId, int entityType, int entityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(entityType, userId);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
                operations.multi();
                operations.opsForZSet().add(followeeKey,entityId,System.currentTimeMillis());
                operations.opsForZSet().add(followerKey,userId,System.currentTimeMillis());
                return operations.exec();
            }
        });
    }

    @Override
    public void unfollow(int userId, int entityType, int entityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey = RedisKeyUtil.getFolloweeKey(entityType, userId);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
                operations.multi();
                operations.opsForZSet().remove(followeeKey, entityId);
                operations.opsForZSet().remove(followerKey, userId);
                return operations.exec();
            }
        });
    }

    @Override
    public long findFolloweeCount(int userId, int entityType) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(entityType, userId);
        Long size = redisTemplate.opsForZSet().zCard(followeeKey);
        return size == null?0 : size;
    }

    @Override
    public long findFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        Long size = redisTemplate.opsForZSet().zCard(followerKey);
        return size == null? 0 : size;
    }

    @Override
    public boolean followStatus(int entityType, int entityId, int userId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        Double score = redisTemplate.opsForZSet().score(followerKey, userId);
        return score != null;
    }

    @Override
    public List<Map<String, Object>> findFollowees(int userId, int entityType, int offset, int limit) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(entityType, userId);
        //set默认是无序的，但是redis的range方法返回的是set这个接口，但是它的实现列是它内置的，是它自己是实现的。是一个有序的集合。这一块它做了一些处理。
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followeeKey, offset, offset + limit -1);

        if(targetIds == null){
            return null;
        }
        List<Map<String, Object>> result = new ArrayList<>();

        for (Integer targetId : targetIds){
            Map<String, Object> map = new HashMap<>();
            User user = userService.findUserById(targetId);
            map.put("user",user);
            Double score = redisTemplate.opsForZSet().score(followeeKey, targetId);
            map.put("followTime",new Date(score.longValue()));
            result.add(map);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> findFollowers(int entityType, int entityId, int offset, int limit) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followerKey, offset, offset + limit - 1);
        if(targetIds == null){
            return null;
        }
        List<Map<String, Object>> result = new ArrayList<>();

        for (Integer targetId : targetIds){
            Map<String, Object> map = new HashMap<>();
            User user = userService.findUserById(targetId);
            map.put("user",user);
            Double score = redisTemplate.opsForZSet().score(followerKey, targetId);
            map.put("followTime",new Date(score.longValue()));
            result.add(map);
        }
        return result;
    }
}
