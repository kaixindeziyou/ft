package com.zrulin.ftcommunity.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author zrulin
 * @create 2022-03-18 8:04
 */
@Data
@ToString
public class Message {
    private Integer id;
    private Integer fromId;
    private Integer toId;
    private String conversationId;
    private String content;
    private Integer status;
    private Date createTime;
}
