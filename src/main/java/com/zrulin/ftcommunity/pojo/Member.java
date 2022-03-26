package com.zrulin.ftcommunity.pojo;

import lombok.Data;

/**
 * @author zrulin
 * @create 2022-03-26 22:29
 */
@Data
public class Member {
    private Integer id;
    private Integer teamId;
    private Integer userId;
    private Integer type;
    private Integer status;
}
