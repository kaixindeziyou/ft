package com.zrulin.ftcommunity.contorller;

import com.zrulin.ftcommunity.pojo.Comment;
import com.zrulin.ftcommunity.pojo.DiscussPost;
import com.zrulin.ftcommunity.pojo.Page;
import com.zrulin.ftcommunity.pojo.User;
import com.zrulin.ftcommunity.service.*;
import com.zrulin.ftcommunity.util.CommonMethod;
import com.zrulin.ftcommunity.util.CommunityConstant;
import com.zrulin.ftcommunity.util.CommunityUtil;
import com.zrulin.ftcommunity.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 展示帖子信息的controller
 * @author zrulin
 * @create 2022-03-09 19:13
 */
@Controller
public class DiscussPostController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeServer likeServer;

    @Autowired
    private CommonMethod commonMethod;

    @Autowired
    private MessageService messageService;

    /**
     * 将帖子表中的信息按照默认或者规定的分页，展示出去。
     * @param model
     * @param page
     * @return
     */
    @GetMapping("/index")
    public String getIndexPage(Model model, Page page){
        //调用方法栈前，SpringMVC会自动实例化Model和Page，并将Page注入Model
        //所以在thymeleaf中可以直接访问Page对象中的数据。
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> discussPosts = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String,Object>> result = new ArrayList<>();
        if(discussPosts != null){
            for (DiscussPost post : discussPosts){
                HashMap<String,Object> map = new HashMap<>();
                User user = userService.findUserById(post.getUserId());
                map.put("post",post);
                //查询帖子赞的数量
                long entityLikeCount = likeServer.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount",entityLikeCount);
                map.put("interval",getInterval(post.getCreateTime()));
                map.put("user",user);
                result.add(map);
            }
        }
        model.addAttribute("discussPosts",result);
        return "/index";
    }
    private String getInterval(Date time){
        Date now = new Date();
        String result = "";
        long diff = now.getTime() - time.getTime();
        diff /= 1000;
        if(diff < 60){
            return diff + "秒前";
        }else if(diff < 60 * 60){
            return (diff / 60) + "分钟前";
        }else if(diff < 60 * 60 * 24){
            return (diff / (60*60)) + "小时前";
        }else {
            return (diff / (60*60 * 24)) + "天前";
        }
    }

    @PostMapping("/discuss/add")
    @ResponseBody
    public String addDiscuss(String title, String content){
        User user = hostHolder.getUser();
        if(user == null){
            return CommunityUtil.getJsonString(403,"您还没有登录！");
        }
        if(StringUtils.isBlank(title) || StringUtils.isBlank(content)){
            return CommunityUtil.getJsonString(403,"标题或内容不能为空！");
        }
        DiscussPost post = new DiscussPost(null, user.getId(), title, content, 0, 0, new Date(), 0, 0.0);
        discussPostService.addDiscussPost(post);
        return CommunityUtil.getJsonString(200,"发布成功");
    }

    @GetMapping("/discuss/detail/{postId}")
    public String findPostDetail(@PathVariable("postId") Integer postId, Model model, Page page){
        //帖子
        DiscussPost postDetail = discussPostService.findPostDetail(postId);
        model.addAttribute("post",postDetail);
        //作者
        User user = userService.findUserById(postDetail.getUserId());
        model.addAttribute("user",user);
        //帖子点赞数量和状态
        int status = user == null?
                0: likeServer.findUserLikeStatus(user.getId(), ENTITY_TYPE_POST, postDetail.getId());
        long entityLikeCount = likeServer.findEntityLikeCount(ENTITY_TYPE_POST, postDetail.getId());
        model.addAttribute("likeCount",entityLikeCount);
        model.addAttribute("likeStatus",status);

        //评论分页信息
        page.setPath("/discuss/detail/"+postId);
        page.setRows(postDetail.getCommentCount());

        //评论：给帖子的评论
        //回复：给评论的评论
        //评论列表
        List<Comment> commentList = commentService.findCommentByEntity(ENTITY_TYPE_POST,
                postDetail.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> commentVOList = commonMethod.commentsAndReplays(commentList, user);
        model.addAttribute("comments",commentVOList);
        return "/site/discuss-detail";
    }
}
