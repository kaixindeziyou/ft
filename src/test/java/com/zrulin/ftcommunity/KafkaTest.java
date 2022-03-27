package com.zrulin.ftcommunity;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author zrulin
 * @create 2022-03-27 19:48
 */
@SpringBootTest
public class KafkaTest {
    @Autowired
    private KafkaProduce kafkaProduce;

    @Test
    public void test(){
        kafkaProduce.sendMessage("test","你好");
        kafkaProduce.sendMessage("test","hello kafka!");
        kafkaProduce.sendMessage("test","再见");

        try {
            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

@Component
class KafkaProduce{

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMessage(String topic, String content){
        //topic 消息主题， content。。
        kafkaTemplate.send(topic,content);
    }
}

@Component
class KafkaConsumer{

    //调用这个方法的时候会把消息封装成ConsumerRecord，从这个record里面就可以读到消息
    @KafkaListener(topics = {"test"})
    public void handMessages(ConsumerRecord record){
        System.out.println(record.value());
    }
}