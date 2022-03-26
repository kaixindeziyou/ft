package com.zrulin.ftcommunity.service;

import com.zrulin.ftcommunity.pojo.Member;
import com.zrulin.ftcommunity.pojo.Team;

import java.util.Date;
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

    /**
     * 点击申请入队
     * @param userId
     * @param teamId
     */
    public void addMember(int userId, int teamId, int type);


    /**
     * 查找队员入队情况
     * @param userId
     * @param teamId
     * @return
     */
    public Member findMember(int userId, int teamId);

    /**
     * 修改队员入队情况
     */
    public int updateMemberStatus(int userId, int teamId, int status);

    /**
     * 增加队伍
     * @param userId
     * @param teamId
     * @return
     */
    public int addTeam(String name, Date endTime, Integer userId, String email, Integer needPerson, String introduce, String activityName);
}
