package com.zrulin.ftcommunity.dao;

import com.zrulin.ftcommunity.pojo.Activity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zrulin
 * @create 2022-03-25 16:33
 */
@Mapper
public interface ActivityMapper {

    /**
     * 获取所有比赛信息
     * @param offset
     * @param limit
     * @return
     */
    public List<Activity> selectActivity(int offset, int limit);

    /**
     * 获取比赛的总条数
     * @return
     */
    public int selectActivityRows();

    /**
     * 通过id找比赛信息
     * @param activityId
     * @return
     */
    public Activity selectActivityById(int activityId);

    /**
     * 修改活动评论数量
     * @param activityId
     * @return
     */
    public int updateCommentCount(int activityId, int commentCount);

    /**
     * 新增活动
     * @param activity
     * @return
     */
    public int insertActivity(Activity activity);
}
