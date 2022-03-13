package com.zrulin.ftcommunity.util;

/**
 * 常量接口
 * @author zrulin
 * @create 2022-03-11 11:18
 */
public interface CommunityConstant {
    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;

    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;
    /**
     * 默认的凭证超时时间
     */
    int DEFAULT_EXPIRED_SECONDS =3600*12;
    /**
     * 记录状态下的登录凭证超时时间
     */
     int REMEMBER_EXPIRED_SECONDS =3600*24*7;
}
