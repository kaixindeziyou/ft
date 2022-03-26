package com.zrulin.ftcommunity.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author zrulin
 * @create 2022-03-15 17:47
 */
@Data
public class Comment {
    private Integer id;
    private Integer userId;
    private Integer entityType;
    private Integer entityId;
    private Integer targetId;
    private String content;
    private Integer status;
    private Date createTime;
}
