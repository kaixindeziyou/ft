package com.zrulin.ftcommunity.dao;

import com.zrulin.ftcommunity.pojo.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zrulin
 * @create 2022-03-18 8:07
 */
@Mapper
public interface MessageMapper {
    /**
     * 查询当前用户的会话列表(分页),针对每个会话只返回一条最新的私信
     * @return
     */
    public List<Message> selectConversations(int userId, int offset, int limit);

    /**
     * 查询当前用户的会话数量
     * @param userId
     * @return
     */
    public int selectConversationCounts(int userId);

    /**
     * 查询当前会话的所有私信(支持分页)
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> selectLetters(String conversationId, int offset, int limit);

    /**
     * 查询某个会话包含的私信数量
     * @param conversationId
     * @return
     */
    public int selectLetterCounts(String conversationId);

    /**
     * 查询未读的消息数量，conversation是动态的参数，sql语句中进行拼接，可以是查找未读的会话数量，也可以是查询会话中的未读消息数量
     * @param UserId
     * @param conversationId
     * @return
     */
    public int selectLetterUnreadCount(int userId, String conversationId);

    /**
     * 增加私信
     * @param message
     * @return
     */
    public int insertMessage(Message message);

    /**
     * 更新一组私信的状态
     * @param ids
     * @param status
     * @return
     */
    public int updateStatus(List<Integer> ids, int status);

    /**
     * 查询某个主题下的最新通知
     * @param userId
     * @param topic
     * @return
     */
    public Message selectLatestNotice(int userId, String topic);

    /**
     * 查询某个主题所包含的通知数量
     * @param userId
     * @param topic
     * @return
     */
    public int selectNoticeCount(int userId, String topic);

    /**
     * 查询（某个主题）所未读通知数量
     * topic 为空的时候，查询所有主题的未读通知数量
     * @param userId
     * @param topic
     * @return
     */
    public int selectNoticeUnreadCount(int userId, String topic);

    /**
     * 查询所有未读消息数量
     * @param userId
     * @return
     */
    public int selectAllUnread(int userId);

    /**
     * 按照主题查找系统通知列表
     * @param userId
     * @param topic
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> selectNotice(int userId, String topic, int offset, int limit);

}
