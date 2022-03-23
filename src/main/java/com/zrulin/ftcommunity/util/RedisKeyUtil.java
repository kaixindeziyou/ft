package com.zrulin.ftcommunity.util;

/**
 * 在向redis存数据取数据，操作数据的过程中，它是以这个key为关键的，它是面向这个key的。
 * 所以为了让这个key能够反复的复用，容易记，写了这样一个工具专门生成这个key
 * @author zrulin
 * @create 2022-03-22 16:57
 */
public class RedisKeyUtil {
    //我们的key都是由冒号隔开的，中间由一个一个单词拼成的。实际开发的时候，有些单词是固定的，有些单词是需要传入的，动态的。

    private static final  String SPLIT = ":";
    //用来存实体的赞，用一个前缀开头
    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    //用来存用户所收到赞的数量
    private static final String PREFIX_USER_LIKE = "like:user";


    /**
     * 生成某个实体的赞的key
     * like:entity:entityType:entityId  ->    set（userId）
     * 这个key所对应的值用一个set（userId），而不是简单的一个整数
     * 因为如果将来我们的需求变了，需要知道是谁点的赞，用set能够满足。set里面存userId
     * @return
     */
    public static String getEntityLikeKey(int EntityType, int EntityId){
        return PREFIX_ENTITY_LIKE + SPLIT + EntityType + SPLIT+EntityId;
    }

    /**
     * 某个用户的赞
     * like:user:userId   -> int
     * @param userId
     * @return
     */
    public static String getUserKey(int userId){
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

}
