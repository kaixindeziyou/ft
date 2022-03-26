package com.zrulin.ftcommunity.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author zrulin
 * @create 2022-03-12 9:49
 */
@Data
public class LoginTicket {
    private Integer id;
    private Integer userId;
    private String ticket;
    private Integer status; // 0-正常 1-无效
    private Date expired;

    public LoginTicket() {
    }

    public LoginTicket(Integer id, Integer userId, String ticket, Integer status, Date expired) {
        this.id = id;
        this.userId = userId;
        this.ticket = ticket;
        this.status = status;
        this.expired = expired;
    }
}
