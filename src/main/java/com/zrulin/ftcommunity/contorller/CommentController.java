package com.zrulin.ftcommunity.contorller;

import com.zrulin.ftcommunity.annotation.LoginRequired;
import com.zrulin.ftcommunity.pojo.Comment;
import com.zrulin.ftcommunity.service.CommentService;
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

    @LoginRequired
    @PostMapping("/add/{discussPostId}")
    public String addComment(
            @PathVariable("discussPostId") int discussPostId,
            Comment comment
    ){
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);
        return "redirect:/discuss/detail/" + discussPostId;
    }
    @PostMapping("/activityAdd/{activityId}")
    public String activityAddComment(
            @PathVariable("activityId") int activityId,
            Comment comment
    ){
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);
        return "redirect:/activity/detail/" + activityId;
    }
}
