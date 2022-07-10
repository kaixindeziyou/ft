package com.zrulin.ftcommunity.config.event;

import com.alibaba.fastjson.JSONObject;
import com.zrulin.ftcommunity.dao.elasticsearch.DiscussPostRepository;
import com.zrulin.ftcommunity.pojo.DiscussPost;
import com.zrulin.ftcommunity.pojo.Event;
import com.zrulin.ftcommunity.pojo.Message;
import com.zrulin.ftcommunity.service.DiscussPostService;
import com.zrulin.ftcommunity.service.ElasticsearchService;
import com.zrulin.ftcommunity.service.MessageService;
import com.zrulin.ftcommunity.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zrulin
 * @create 2022-03-27 20:36
 */
@Component
public class EventConsumer implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @KafkaListener(topics = {TOPIC_Like,TOPIC_COMMENT,TOPIC_FOLLOW})
    public void handelCommentMessage(ConsumerRecord record){
        if(record == null || record.value() == null){
            logger.error("订阅的消息内容为空");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if(event == null){
            logger.error("订阅的消息格式错误！");
            return;
        }

        //发送站内通知,构造message对象，复用表。
        Message message = new Message();
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setStatus(0);
        message.setCreateTime(new Date());

        Map<String, Object> content = new HashMap<>();
        content.put("userId",event.getUserId());
        content.put("entityType",event.getEntityType());
        content.put("entityId",event.getEntityId());

        if(!event.getMap().isEmpty()){
            for(Map.Entry<String, Object> map : event.getMap().entrySet()){
                content.put(map.getKey(),map.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);
    }

    @KafkaListener(topics = {TOPIC_PUBLISH})
    public void handlePublishMessage(ConsumerRecord record){
        if(record == null || record.value() == null){
            logger.error("订阅的消息内容为空");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if(event == null){
            logger.error("订阅的消息格式错误！");
            return;
        }

        DiscussPost post = discussPostService.findPostDetail(event.getEntityId());
        elasticsearchService.saveDiscussPost(post);
    }
}
