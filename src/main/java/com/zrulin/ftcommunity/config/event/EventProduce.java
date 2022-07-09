package com.zrulin.ftcommunity.config.event;

import com.alibaba.fastjson.JSONObject;
import com.zrulin.ftcommunity.pojo.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author zrulin
 * @create 2022-03-27 20:34
 */
@Component
public class EventProduce {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void fireEvent(Event event){
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }
}
