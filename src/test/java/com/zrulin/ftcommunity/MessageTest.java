package com.zrulin.ftcommunity;

import com.zrulin.ftcommunity.dao.MessageMapper;
import com.zrulin.ftcommunity.pojo.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

/**
 * @author zrulin
 * @create 2022-03-18 9:51
 */
@SpringBootTest
public class MessageTest {

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void test(){
        List<Message> messages = messageMapper.selectConversations(13, 0, 3);
        for (Message message: messages){
            System.out.println(message);
        }
    }
    @Test
    public void test1(){
        int i = messageMapper.selectConversationCounts(13);
        System.out.println(i);
    }
    @Test
    public void test2(){
        for (Message message : messageMapper.selectLetters("13_16", 0, 5)) {
            System.out.println(message);
        }
        int i = messageMapper.selectLetterCounts("13_16");
        System.out.println(i);
    }

    @Test
    public void test3(){
        int i = messageMapper.selectLetterUnreadCount(13, null);
        System.out.println(i);
        int i1 = messageMapper.selectLetterUnreadCount(13, "13_16");
        System.out.println(i1);

    }
    @Test
    public void test4(){
        Message message = new Message();
        message.setFromId(13);
        message.setContent("今晚9点三国杀，不见不散");
        message.setToId(19);
        message.setConversationId("13_19");
        message.setStatus(0);
        message.setCreateTime(new Date());
        messageMapper.insertMessage(message);
    }
}
