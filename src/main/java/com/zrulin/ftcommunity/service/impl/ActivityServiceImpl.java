package com.zrulin.ftcommunity.service.impl;

import com.zrulin.ftcommunity.dao.ActivityMapper;
import com.zrulin.ftcommunity.pojo.Activity;
import com.zrulin.ftcommunity.service.ActivityService;
import com.zrulin.ftcommunity.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author zrulin
 * @create 2022-03-25 16:56
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Override
    public List<Activity> findActivity(int offset, int limit) {
        return activityMapper.selectActivity(offset, limit);
    }

    @Override
    public int findActivityRows() {
        return activityMapper.selectActivityRows();
    }

    @Override
    public Activity findActivityById(int activityId) {
        return activityMapper.selectActivityById(activityId);
    }

    @Override
    public int addActivity(Activity activity) {
        if(activity == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //转义HTML标记
        activity.setTitle(HtmlUtils.htmlEscape(activity.getTitle()));
        activity.setContent(HtmlUtils.htmlEscape(activity.getContent()));
        //过滤敏感词
        activity.setTitle(sensitiveFilter.filter(activity.getTitle()));
        activity.setContent(sensitiveFilter.filter(activity.getContent()));

        return activityMapper.insertActivity(activity);
    }
}
