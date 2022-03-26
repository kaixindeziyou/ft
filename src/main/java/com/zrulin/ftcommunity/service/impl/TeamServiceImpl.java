package com.zrulin.ftcommunity.service.impl;

import com.zrulin.ftcommunity.dao.TeamMapper;
import com.zrulin.ftcommunity.pojo.Member;
import com.zrulin.ftcommunity.pojo.Team;
import com.zrulin.ftcommunity.service.TeamService;
import com.zrulin.ftcommunity.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

/**
 * @author zrulin
 * @create 2022-03-26 14:48
 */
@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Override
    public List<Team> findTeams(int offset, int limit) {
        return teamMapper.selectTeams(offset, limit);
    }

    @Override
    public int findTeamRows() {
        return teamMapper.selectTeamRows();
    }

    @Override
    public Team findTeamById(int teamId) {
        return teamMapper.selectTeamById(teamId);
    }

    @Override
    public void addMember(int userId, int teamId, int type) {
        Member member = new Member();
        member.setUserId(userId);
        member.setTeamId(teamId);
        member.setStatus(0);
        member.setType(type);
        teamMapper.insertMember(member);
    }

    @Override
    public Member findMember(int userId, int teamId) {
        return teamMapper.selectMember(userId,teamId);
    }

    @Override
    public int updateMemberStatus(int userId, int teamId, int status) {
        return teamMapper.updateMemberStatus(userId,teamId,status);
    }


    @Override
    public int addTeam(String name, Date endTime, Integer userId, String email, Integer needPerson, String introduce, String activityName) {
        name = HtmlUtils.htmlEscape(name);
        name = sensitiveFilter.filter(name);
        introduce = HtmlUtils.htmlEscape(introduce);
        introduce = sensitiveFilter.filter(introduce);

        Team team = new Team();
        team.setName(name);
        team.setUserId(userId);
        team.setEmail(email);
        team.setNeedPerson(needPerson);
        team.setPerson(0);
        team.setIntroduce(introduce);
        team.setActivityName(activityName);
        team.setCreateTime(new Date());
        team.setEndTime(endTime);
        team.setStatus(0);
        teamMapper.insertTeam(team);
        return team.getId();
    }
}
