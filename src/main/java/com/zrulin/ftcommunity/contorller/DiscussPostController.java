package com.zrulin.ftcommunity.contorller;

import com.zrulin.ftcommunity.pojo.DiscussPost;
import com.zrulin.ftcommunity.pojo.Page;
import com.zrulin.ftcommunity.pojo.User;
import com.zrulin.ftcommunity.service.DiscussPostService;
import com.zrulin.ftcommunity.service.UserService;
import com.zrulin.ftcommunity.service.impl.DiscussPostServiceImpl;
import com.zrulin.ftcommunity.service.impl.UserServiceImpl;
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

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 展示帖子信息的controller
 * @author zrulin
 * @create 2022-03-09 19:13
 */
@Controller
public class DiscussPostController {
    @Autowired
    private DiscussPostServiceImpl discussPostService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private HostHolder hostHolder;

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
                map.put("user",user);
                result.add(map);
            }
        }
        model.addAttribute("discussPosts",result);
        return "/index";
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
    public String findPostDetail(@PathVariable("postId") Integer postId, Model model){
        //帖子
        DiscussPost postDetail = discussPostService.findPostDetail(postId);
        model.addAttribute("post",postDetail);
        //作者
        User user = userService.findUserById(postDetail.getUserId());
        model.addAttribute("user",user);
        return "/site/discuss-detail";
    }
}
