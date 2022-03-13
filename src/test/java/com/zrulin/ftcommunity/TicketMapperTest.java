package com.zrulin.ftcommunity;

import com.zrulin.ftcommunity.dao.LoginTicketMapper;
import com.zrulin.ftcommunity.pojo.LoginTicket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * @author zrulin
 * @create 2022-03-12 10:22
 */
@SpringBootTest
public class TicketMapperTest {
    @Autowired
    private LoginTicketMapper ticketMapper;
    @Test
    public void test1(){
        ticketMapper.insertTicket(new LoginTicket(null,1,"sjotgowij4234",0,new Date()));
    }
    @Test
    public void test2(){
        LoginTicket ticket = ticketMapper.selectByTicket("sjotgowij4234");
        System.out.println(ticket);
    }

    @Test
    public void test3(){
        ticketMapper.updateStatus("sjotgowij4234",1);
    }
}
