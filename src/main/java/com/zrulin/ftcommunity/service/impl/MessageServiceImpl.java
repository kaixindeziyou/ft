package com.zrulin.ftcommunity.service.impl;

import com.zrulin.ftcommunity.dao.MessageMapper;
import com.zrulin.ftcommunity.pojo.Message;
import com.zrulin.ftcommunity.service.MessageService;
import com.zrulin.ftcommunity.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author zrulin
 * @create 2022-03-18 10:08
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public List<Message> findConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId,offset,limit);
    }

    @Override
    public int findConversationCount(int userId) {
        return messageMapper.selectConversationCounts(userId);
    }

    @Override
    public List<Message> findLetters(String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId,offset,limit);
    }

    @Override
    public int findLetterCount(String conversationId) {
        return messageMapper.selectLetterCounts(conversationId);
    }

    @Override
    public int findLetterUnreadCount(int userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId,conversationId);
    }

    @Override
    public int addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    @Override
    public int updateMessagesStatus(List<Integer> ids) {
        return messageMapper.updateStatus(ids,1);
    }
}
