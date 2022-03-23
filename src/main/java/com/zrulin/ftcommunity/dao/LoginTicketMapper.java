package com.zrulin.ftcommunity.dao;

import com.zrulin.ftcommunity.pojo.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @author zrulin
 * @create 2022-03-12 9:51
 */
@Mapper
@Deprecated
public interface LoginTicketMapper {

    /**
     * 登录的时候插入一条凭证
     * @return
     */
    @Insert({
            "insert into login_ticket (user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true,keyProperty = "id")
    public Integer insertTicket(LoginTicket ticket);

    /**
     * 我们最终是把ticket凭证发送给浏览器进行保存
     * 其他的是我们服务端进行保存
     * @param ticket
     * @return
     */
    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket ",
            "where ticket = #{ticket}"
    })
    public LoginTicket selectByTicket(String ticket);

    /**
     * 更新凭证状态
     * @param ticket
     * @param status
     * @return
     */
    @Update({
            "update login_ticket set status = #{status} where ticket = #{ticket}"
    })
    public Integer updateStatus(String ticket,Integer status);
}
