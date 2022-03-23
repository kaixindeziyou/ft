package com.zrulin.ftcommunity.contorller;

import com.zrulin.ftcommunity.annotation.LoginRequired;
import com.zrulin.ftcommunity.service.LikeServer;
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
public class LikeController {
    @Autowired
    private LikeServer likeServer;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @PostMapping("/click")
    @ResponseBody
    public String like(
            int entityType,
            int entityId,
            int entityUserId){
        likeServer.like(hostHolder.getUser().getId(),entityType,entityId,entityUserId);
        //点赞数量
        long entityLikeCount = likeServer.findEntityLikeCount(entityType, entityId);
        //点赞状态
        int status = likeServer.findUserLikeStatus(hostHolder.getUser().getId(), entityType, entityId);

        Map<String, Object> map = new HashMap<>();
        map.put("likeCount",entityLikeCount);
        map.put("status",status);
        return CommunityUtil.getJsonString(0,"成功",map);
    }
}
