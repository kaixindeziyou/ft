package com.zrulin.ftcommunity.service.impl;

import com.zrulin.ftcommunity.dao.TeamMapper;
import com.zrulin.ftcommunity.pojo.Team;
import com.zrulin.ftcommunity.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zrulin
 * @create 2022-03-26 14:48
 */
@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamMapper teamMapper;

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
}
