package com.zrulin.ftcommunity.contorller;

import com.zrulin.ftcommunity.annotation.LoginRequired;
import com.zrulin.ftcommunity.config.event.EventProduce;
import com.zrulin.ftcommunity.pojo.*;
import com.zrulin.ftcommunity.service.ActivityService;
import com.zrulin.ftcommunity.service.CommentService;
import com.zrulin.ftcommunity.service.DiscussPostService;
import com.zrulin.ftcommunity.service.UserService;
import com.zrulin.ftcommunity.util.CommunityConstant;
import com.zrulin.ftcommunity.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * @author zrulin
 * @create 2022-03-17 11:56
 */
@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private EventProduce eventProduce;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    // 话题评论
    @LoginRequired
    @PostMapping("/add/{discussPostId}")
    public String addComment(
            @PathVariable("discussPostId") int discussPostId,
            Comment comment
    ){
        //增加评论记录
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);
        //触发系统通知
        eventStart(comment,discussPostId,hostHolder.getUser().getId());
        return "redirect:/discuss/detail/" + discussPostId;
    }
    //活动评论
    @PostMapping("/activityAdd/{activityId}")
    public String activityAddComment(
            @PathVariable("activityId") int activityId,
            Comment comment
    ){
        //增加评论记录
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);
        eventStart(comment,activityId,hostHolder.getUser().getId());
        return "redirect:/activity/detail/" + activityId;
    }

    private void eventStart(Comment comment, int id,int userId){
        //触发系统通知
        Event event = new Event();
        event.setTopic(TOPIC_COMMENT)
                .setUserId(userId)
                .setEntityUserId(comment.getEntityId())
                .setEntityType(comment.getEntityType());
        if(comment.getEntityType() == ENTITY_TYPE_POST){
            DiscussPost post = discussPostService.findPostDetail(comment.getEntityId());
            event.setEntityUserId(post.getUserId())
                    .setMap("postId",id);
        }else if(comment.getEntityType() == ENTITY_TYPE_COMMENT){
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId())
                    .setMap("postId",id);
        }else if(comment.getEntityType() == ENTITY_TYPE_ACTIVITY){
            Activity activity = activityService.findActivityById(comment.getEntityId());
            User user = userService.findUserByUsername(activity.getIssuer());
            event.setEntityUserId(user.getId())
                    .setMap("activityId",id);
        }
        eventProduce.fireEvent(event);
    }
}
