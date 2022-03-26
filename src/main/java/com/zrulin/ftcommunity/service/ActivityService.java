package com.zrulin.ftcommunity.service;

import com.zrulin.ftcommunity.pojo.Activity;

import java.util.List;

/**
 * @author zrulin
 * @create 2022-03-25 16:56
 */
public interface ActivityService {

    /**
     * 查询活动信息（支持分页）
     * @param offset
     * @param limit
     * @return
     */
    public List<Activity> findActivity(int offset, int limit);

    /**
     * 查询活动数量
     * @return
     */
    public int findActivityRows();

    /**
     * 通过id查询比赛信息
     * @param activityId
     * @return
     */
    public Activity findActivityById(int activityId);

    /**
     * 增加活动
     * @param activity
     * @return
     */
    public int addActivity(Activity activity);
}
