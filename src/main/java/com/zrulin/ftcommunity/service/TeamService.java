package com.zrulin.ftcommunity.service;

import com.zrulin.ftcommunity.pojo.Team;

import java.util.List;

/**
 * @author zrulin
 * @create 2022-03-26 14:46
 */
public interface TeamService {

    /**
     * 查找所有比赛队伍
     * @param offset
     * @param limit
     * @return
     */
    public List<Team> findTeams(int offset, int limit);

    /**
     * 查找队伍总条数
     * @return
     */
    public int findTeamRows();

    /**
     * 根据id查找队伍
     * @param teamId
     * @return
     */
    public Team findTeamById(int teamId);
}
