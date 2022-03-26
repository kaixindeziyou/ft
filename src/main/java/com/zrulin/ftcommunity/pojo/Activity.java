package com.zrulin.ftcommunity.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author zrulin
 * @create 2022-03-25 16:35
 */
@Data
public class Activity {
    private Integer id;
    private String issuer;
    private Date startTime;
    private Date endTime;
    private String title;
    private String content;
    private String photo;
    private String email;
    private Date createTime;
    private Integer status;
    private Integer commentCount;
}
