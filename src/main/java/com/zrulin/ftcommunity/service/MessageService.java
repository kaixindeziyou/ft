package com.zrulin.ftcommunity.service;

import com.zrulin.ftcommunity.pojo.Message;

import java.util.List;

/**
 * @author zrulin
 * @create 2022-03-18 10:03
 */
public interface MessageService {
    /**
     * 查找当前用户的会话列表（支持分页）
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> findConversations(int userId, int offset, int limit);

    /**
     * 查找当前用户会话的数量
     * @param userId
     * @return
     */
    public int findConversationCount(int userId);

    /**
     * 查询当前会话包含的私信（支持分页）
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> findLetters(String conversationId, int offset, int limit);

    /**
     * 查询当前会话私信的数量
     * @param conversationId
     * @return
     */
    public int findLetterCount(String conversationId);

    /**
     * 查询当前用户未读的所有未读信息数量，或者某一个会话中的未读信息数量
     * @param userId
     * @param conversationId
     * @return
     */
    public int findLetterUnreadCount(int userId, String conversationId);

    /**
     * 添加一条消息
     * @param message
     * @return
     */
    public int addMessage(Message message);

    /**
     * 把多条消息的状态改为已读
     * @param ids
     * @return  返回的整数表示影响的行数
     */
    public int updateMessagesStatus(List<Integer> ids);

    /**
     * 某个主题最新的通知
     * @param userId
     * @param topic
     * @return
     */
    public Message findLatestNotice(int userId, String topic);

    /**
     * 某个主题的消息数量
     * @param userId
     * @param topic
     * @return
     */
    public int findNoticeCount(int userId, String topic);

    /**
     * （某个主题）的未读通知数量
     * @param userId
     * @param topic
     * @return
     */
    public int findNoticeUnreadCount(int userId, String topic);

    /**
     * 查询所有未读消息数量
     * @param userId
     * @return
     */
    public int findAllUnread(int userId);

    /**
     * 根据主题查找系统通知列表
     * @param userId
     * @param topic
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> findNotices(int userId, String topic, int offset, int limit);
}
