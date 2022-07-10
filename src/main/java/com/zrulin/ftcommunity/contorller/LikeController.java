package com.zrulin.ftcommunity.contorller;

import com.zrulin.ftcommunity.annotation.LoginRequired;
import com.zrulin.ftcommunity.config.event.EventProduce;
import com.zrulin.ftcommunity.pojo.Event;
import com.zrulin.ftcommunity.service.LikeService;
import com.zrulin.ftcommunity.util.CommunityConstant;
import com.zrulin.ftcommunity.util.CommunityUtil;
import com.zrulin.ftcommunity.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zrulin
 * @create 2022-03-22 17:26
 */
@Controller
@RequestMapping("/like")
public class LikeController implements CommunityConstant {
    @Autowired
    private LikeService likeServer;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProduce eventProduce;

    @LoginRequired
    @PostMapping("/click")
    @ResponseBody
    public String like(
            int entityType,
            int entityId,
            int entityUserId,
            int postId){
        //点赞
        likeServer.like(hostHolder.getUser().getId(),entityType,entityId,entityUserId);

        //点赞数量
        long entityLikeCount = likeServer.findEntityLikeCount(entityType, entityId);
        //点赞状态
        int status = likeServer.findUserLikeStatus(hostHolder.getUser().getId(), entityType, entityId);

        Map<String, Object> map = new HashMap<>();
        map.put("likeCount",entityLikeCount);
        map.put("status",status);

        //触发系统通知
        if(status == 1){
            Event event = new Event()
                    .setTopic(TOPIC_Like)
                    .setUserId(hostHolder.getUser().getId())
                    .setEntityId(entityId)
                    .setEntityType(entityType)
                    .setEntityUserId(entityUserId)
                    .setMap("postId",postId);
            eventProduce.fireEvent(event);
        }

        return CommunityUtil.getJsonString(0,"成功",map);
    }
}
