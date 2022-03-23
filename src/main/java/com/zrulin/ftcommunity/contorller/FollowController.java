package com.zrulin.ftcommunity.contorller;

import com.sun.jmx.snmp.daemon.CommunicatorServerMBean;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.zrulin.ftcommunity.annotation.LoginRequired;
import com.zrulin.ftcommunity.pojo.Page;
import com.zrulin.ftcommunity.pojo.User;
import com.zrulin.ftcommunity.service.FollowService;
import com.zrulin.ftcommunity.service.UserService;
import com.zrulin.ftcommunity.util.CommunityConstant;
import com.zrulin.ftcommunity.util.CommunityUtil;
import com.zrulin.ftcommunity.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author zrulin
 * @create 2022-03-23 10:30
 */
@Controller
public class FollowController implements CommunityConstant {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @LoginRequired
    @PostMapping("/follow")
    @ResponseBody
    public String follow(int entityType, int entityId){
        followService.follow(hostHolder.getUser().getId(),entityType,entityId);
        return CommunityUtil.getJsonString(0,"已关注");
    }
    @LoginRequired
    @PostMapping("/unfollow")
    @ResponseBody
    public String unfollow(int entityType, int entityId){
        followService.unfollow(hostHolder.getUser().getId(),entityType,entityId);
        return CommunityUtil.getJsonString(0,"已取消关注");
    }

    /**
     * 查找某个用户关注的-用户们
     * @param userId
     * @param model
     * @return
     */
    @GetMapping("/followees/{userId}")
    public String findFollowees(@PathVariable("userId") int userId,
                                Model model,
                                Page page){
        User user = userService.findUserById(userId);
        if(user == null){
            throw new RuntimeException("该用户不存在！");
        }
        model.addAttribute("user",user);
        //处理分页
        page.setPath("/followees/" + userId);
        page.setRows((int) followService.findFolloweeCount(userId,ENTITY_TYPE_USER));

        List<Map<String, Object>> followees = followService.findFollowees(userId, ENTITY_TYPE_USER, page.getOffset(), page.getLimit());
        if(followees != null){
            for (Map<String, Object> map : followees){
                User u = (User) map.get("user");
                map.put("followStatus",hasFollowed(u.getId()));
            }
        }
        model.addAttribute("followees",followees);
        return "site/followee";
    }

    @GetMapping("/followers/{userId}")
    public String findFollowers(@PathVariable("userId") int userId,
                                Model model,
                                Page page){
        User user = userService.findUserById(userId);
        if(user == null){
            throw new RuntimeException("该用户不存在！");
        }
        model.addAttribute("user",user);
        //处理分页
        page.setPath("/followers/" + userId);
        page.setRows((int) followService.findFollowerCount(ENTITY_TYPE_USER, userId));

        List<Map<String, Object>> followers = followService.findFollowers(ENTITY_TYPE_USER,userId,page.getOffset(),page.getLimit());
        if(followers != null){
            for (Map<String, Object> map : followers){
                User u = (User) map.get("user");
                map.put("followStatus",hasFollowed(u.getId()));
            }
        }
        model.addAttribute("followers",followers);
        return "site/follower";
    }

    private boolean hasFollowed(int userId){
        if(hostHolder.getUser() == null){
            return false;
        }
        return followService.followStatus(ENTITY_TYPE_USER, userId, hostHolder.getUser().getId());
    }

}
