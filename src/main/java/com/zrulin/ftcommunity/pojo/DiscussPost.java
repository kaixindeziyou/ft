package com.zrulin.ftcommunity.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author zrulin
 * @create 2022-03-09 16:56
 */
@Data
@ToString
public class DiscussPost {
    private Integer id;
    //显示的时候肯定不会显示userId，坑定会显示名称，两种办法：
    //第一种是在写sql语句的时候关联，查询用户，然后去处理，把用户的数据一起查到
    //第二种方式是，得到这个数据以后单独的根据这个数据（DiscussPost），单独的查一下user，然后把查到的user和这个DiscussPost组合在一起返回给页面。
    //采用后者，因为这样的方式看起来好像麻烦点，但是将来使用Redis去缓存一些数据的时候比较方便，到那个时候性能高，代码看起来也直观。
    private Integer userId;
    private String title;
    private String content;
    private int type;
    private int status;
    private Date createTime;
    private int commentCount;
    private Double score;

    public DiscussPost() {
    }

    public DiscussPost(Integer id, Integer userId, String title, String content, int type, int status, Date createTime, int commentCount, Double score) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.type = type;
        this.status = status;
        this.createTime = createTime;
        this.commentCount = commentCount;
        this.score = score;
    }
}
