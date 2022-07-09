package com.zrulin.ftcommunity;

import com.zrulin.ftcommunity.dao.TeamMapper;
import com.zrulin.ftcommunity.pojo.Member;
import com.zrulin.ftcommunity.pojo.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * @author zrulin
 * @create 2022-03-26 14:43
 */
@SpringBootTest
public class TeamTest {
    @Autowired
    private TeamMapper teamMapper;

    @Test
    public void test10(){
        System.out.println(teamMapper);
    }
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
    @Test
    public void test2(){
        Member member = teamMapper.selectMember(13, 1);
        System.out.println(member);
    }

    @Test
    public void test3(){
        teamMapper.updateMemberStatus(13,2,1);
    }

    @Test
    public void test4(){
        Member member = new Member();
        member.setTeamId(1);
        member.setStatus(1);
        member.setType(0);
        member.setUserId(22);
        int i = teamMapper.insertMember(member);
    }

    @Test
    public void test5(){
        Team team = new Team();
        team.setName("ddd");
        team.setUserId(13);
        team.setEmail("dddd");
        team.setNeedPerson(12);
        team.setPerson(0);
        team.setIntroduce("ddddd");
        team.setActivityName("ddd");
        team.setCreateTime(new Date());
        team.setEndTime(new Date());
        team.setStatus(0);
        int i = teamMapper.insertTeam(team);
    }
}
