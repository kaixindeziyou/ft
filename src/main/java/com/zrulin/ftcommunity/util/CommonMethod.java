package com.zrulin.ftcommunity.util;

import com.zrulin.ftcommunity.pojo.Comment;
import com.zrulin.ftcommunity.pojo.User;
import com.zrulin.ftcommunity.service.CommentService;
import com.zrulin.ftcommunity.service.LikeService;
import com.zrulin.ftcommunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zrulin
 * @create 2022-03-25 21:38
 */
@Component
public class CommonMethod implements CommunityConstant {
    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeServer;

    @Autowired
    private CommentService commentService;

    public List<Map<String,Object>> commentsAndReplays(List<Comment> commentList, User user){
        List<Map<String,Object>> commentVOList = new ArrayList<>();
        if(commentList != null){
            for(Comment comment: commentList){
                //评论VO
                Map<String,Object> commentVO = new HashMap<>();
                //评论
                commentVO.put("comment",comment);
                //作者
                commentVO.put("user",userService.findUserById(comment.getUserId()));
                //评论帖子点赞数量和状态
                int commentStatus = user == null?
                        0: likeServer.findUserLikeStatus(user.getId(), ENTITY_TYPE_COMMENT, comment.getId());
                long commentLikeCount = likeServer.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVO.put("likeCount",commentLikeCount);
                commentVO.put("likeStatus",commentStatus);
                //回复列表
                List<Comment> replayList = commentService.findCommentByEntity(ENTITY_TYPE_COMMENT,
                        comment.getId(), 0, Integer.MAX_VALUE);
                List<Map<String,Object>> replayVOList = new ArrayList<>();
                if(replayList != null){
                    for(Comment replay : replayList){
                        //回复VO
                        Map<String,Object> replayVO = new HashMap<>();
                        //回复
                        replayVO.put("replay",replay);
                        //作者
                        replayVO.put("user",userService.findUserById(replay.getUserId()));
                        //回复帖子点赞数量和状态
                        int replayStatus = user == null?
                                0: likeServer.findUserLikeStatus(user.getId(), ENTITY_TYPE_COMMENT, replay.getId());
                        long replayLikeCount = likeServer.findEntityLikeCount(ENTITY_TYPE_COMMENT, replay.getId());
                        replayVO.put("likeCount",replayLikeCount);
                        replayVO.put("likeStatus",replayStatus);
                        //回复目标
                        User targetUser = replay.getTargetId() == 0 ? null : userService.findUserById(replay.getTargetId());
                        replayVO.put("target",targetUser);
                        replayVOList.add(replayVO);
                    }
                }
                commentVO.put("replays",replayVOList);
                //回复数量
                int replayCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVO.put("replayCount",replayCount);
                commentVOList.add(commentVO);
            }
        }
        return commentVOList;
    }
}
