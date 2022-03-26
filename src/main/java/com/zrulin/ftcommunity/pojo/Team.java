package com.zrulin.ftcommunity.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author zrulin
 * @create 2022-03-26 14:15
 */
@Data
public class Team {
    private Integer id;
    private String name;
    private Date endTime;
    private Integer userId;
    private String email;
    private Integer needPerson;
    private Integer person;
    private String introduce;
    private String activityName;
    private String createTime;
    private Integer status;
}
