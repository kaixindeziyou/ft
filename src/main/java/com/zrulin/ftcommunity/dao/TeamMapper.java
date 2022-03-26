package com.zrulin.ftcommunity.dao;

import com.zrulin.ftcommunity.pojo.Member;
import com.zrulin.ftcommunity.pojo.Team;
import org.apache.ibatis.annotations.Mapper;
import org.aspectj.weaver.ast.Test;

import java.util.List;

/**
 * @author zrulin
 * @create 2022-03-26 14:18
 */
@Mapper
public interface TeamMapper {
    /**
     * 搜索队伍列表，按照时间分类
     * @param offset
     * @param limit
     * @return
     */
    public List<Team> selectTeams(int offset, int limit);

    /**
     * 查找队伍条数
     * @return
     */
    public int selectTeamRows();

    /**
     * 更具id找队伍
     * @return
     */
    public Team selectTeamById(int teamId);

    /**
     * 往队员记录里面增加申请记录
     * @param userId
     * @param teamId
     * @return
     */
    public int insertMember(Member member);

    /**
     * 查找用户入队情况
     * @param userId
     * @param teamId
     * @return
     */
    public Member selectMember(int userId, int teamId);

    /**
     * 修改队员入队情况
     * @param userId
     * @param teamId
     * @param status
     * @return
     */
    public int updateMemberStatus(int userId, int teamId, int status);

    /**
     * 增加队伍
     * @param team
     * @return
     */
    public int insertTeam(Team team);
}
