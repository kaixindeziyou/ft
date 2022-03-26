package com.zrulin.ftcommunity;

import com.zrulin.ftcommunity.dao.TeamMapper;
import com.zrulin.ftcommunity.pojo.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zrulin
 * @create 2022-03-26 14:43
 */
@SpringBootTest
public class TeamTest {
    @Autowired
    private TeamMapper teamMapper;
    @Test
    public void test(){
        for (Team selectTeam : teamMapper.selectTeams(0, 3)) {
            System.out.println(selectTeam);
        }
        int i = teamMapper.selectTeamRows();
        System.out.println(i);
    }
    @Test
    public void test1(){
        Team team = teamMapper.selectTeamById(1);
        System.out.println(team);
    }
}
