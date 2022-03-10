package com.zrulin.ftcommunity.contorller;

import com.zrulin.ftcommunity.pojo.DiscussPost;
import com.zrulin.ftcommunity.pojo.Page;
import com.zrulin.ftcommunity.pojo.User;
import com.zrulin.ftcommunity.service.DiscussPostService;
import com.zrulin.ftcommunity.service.UserService;
import com.zrulin.ftcommunity.service.impl.DiscussPostServiceImpl;
import com.zrulin.ftcommunity.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zrulin
 * @create 2022-03-09 19:13
 */
@Controller
public class DiscussPostController {
    @Autowired
    private DiscussPostServiceImpl discussPostService;

    @Autowired
    private UserServiceImpl userService;

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
}
