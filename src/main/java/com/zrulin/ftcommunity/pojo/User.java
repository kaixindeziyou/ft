package com.zrulin.ftcommunity.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author zrulin
 * @create 2022-03-09 18:08
 */
@Data
@ToString
public class User {
    private Integer id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private Integer type;
    private Integer status;
    private String activationCode;
    private String headerUrl;
    private Date createTime;

    public User() {
    }

    public User(Integer id, String username, String password, String salt, String email, Integer type, Integer status, String activationCode, String headerUrl, Date createTime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.email = email;
        this.type = type;
        this.status = status;
        this.activationCode = activationCode;
        this.headerUrl = headerUrl;
        this.createTime = createTime;
    }
}
