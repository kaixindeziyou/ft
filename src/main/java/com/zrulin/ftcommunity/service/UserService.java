package com.zrulin.ftcommunity.service;

import com.zrulin.ftcommunity.pojo.LoginTicket;
import com.zrulin.ftcommunity.pojo.User;

import java.util.Map;

/**
 * @author zrulin
 * @create 2022-03-09 18:53
 */
public interface UserService {

    /**
     * 通过id获取用户信息
     * @param id
     * @return
     */
    public User findUserById(Integer id);

    /**
     * 优先从缓存中取值
     * @param userId
     * @return
     */
    public User getCache(int userId);

    /**
     * 取不到时初始化缓存数据
     * @param userId
     * @return
     */
    public User initCache(int userId);

    /**
     * 数据变更时清除缓存数据
     * 删除要比更新简单， 更新数据可能会有并发的问题，删除比较干脆
     * @param userId
     */
    public void clearCatch(int userId);

    public User findUserByUsername(String username);

    /**
     * 判断合法性，注册，发送邮件
     * @param user
     * @return
     */
    public Map<String,Object> register(User user);

    /**
     * 激活账户
     * @param userId
     * @param code
     * @return
     */
    public Integer activation(Integer userId,String code);


    /**
     * 登录功能实现。
     * @param username
     * @param password
     * @param expiredSeconds  凭证过期的秒数，
     * @return
     */
    public Map<String,Object> login(String username,String password,Integer expiredSeconds);

    /**
     * 推出登录
     * @param ticket
     */
    public void logout(String ticket);

    /**
     * 查询凭证
     */
    public LoginTicket findLoginTicket(String ticket);

    /**
     *更新用户头像
     */
    public int updateHeaderUrl(Integer userId,String url);
    /**
     * 更改密码
     */
    public Map<String,Object> updatePassword(User user , String password,String oldPassword);
    
}
