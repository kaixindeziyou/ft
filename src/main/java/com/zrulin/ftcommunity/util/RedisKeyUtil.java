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

    // 用来处理关注和粉丝
    private static final String PREFIX_FOLLOWEE = "followee";
    private static final String PREFIX_FOLLOWER = "follower";

    // 用来处理验证码
    private static final String PREFIX_KAPTCHA = "kaptcha";

    //用来处理ticket凭证
    private static final String PREFIX_TICKET = "ticket";

    //用来处理缓存常用的用户数据
    private static final String PREFIX_USER = "user";


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

    /**
     * 某个用户关注的实体
     *  followee:userId:entityType -> zset(entityId , now Date)
     * @param entityType
     * @param userId
     * @return
     */
    public static String getFolloweeKey(int entityType, int userId){
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    /**
     * 某个实体拥有的粉丝
     * follower:entityType:entityId  -> zset(userId now Date)
     * @param entityType
     * @param entityId
     * @return
     */
    public static String getFollowerKey(int entityType, int entityId){
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    // 登录验证码
    public static String getKaptchaKey(String owner){
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    // 登录的凭证
    public static String getTicketKey(String ticket){
        return PREFIX_TICKET + SPLIT + ticket;
    }

    // 缓存常用的用户
    public static String getCommonUserKey(int userId){
        return PREFIX_USER + SPLIT + userId;
    }
}
