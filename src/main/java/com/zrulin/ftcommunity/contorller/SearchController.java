package com.zrulin.ftcommunity.contorller;

import com.zrulin.ftcommunity.pojo.DiscussPost;
import com.zrulin.ftcommunity.pojo.Page;
import com.zrulin.ftcommunity.service.ElasticsearchService;
import com.zrulin.ftcommunity.service.LikeService;
import com.zrulin.ftcommunity.service.UserService;
import com.zrulin.ftcommunity.util.CommunityConstant;
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
 * @create 2022-07-09 15:47
 */
@Controller
public class SearchController implements CommunityConstant {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @GetMapping("/search")
    public String search(String keyword, Page page, Model model){
        //搜索帖子
        Map<String, Object> result = elasticsearchService.searchDiscussPost(keyword, page.getCurrent() - 1, page.getLimit());
        List<DiscussPost> discussPosts = (List<DiscussPost>) result.get("posts");
        //聚合数据
        List<Map<String, Object>> list = new ArrayList<>();
        if(discussPosts != null){
            for(DiscussPost post : discussPosts){
                Map<String, Object> map = new HashMap<>();
                //帖子
                map.put("post", post);
                //作者
                map.put("user", userService.findUserById(post.getUserId()));
                //点赞数量
                map.put("likeCount", likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId()));

                list.add(map);
            }
        }
        model.addAttribute("discussPosts", list);
        model.addAttribute("keyword" , keyword);
        //设置分页
        page.setPath("/search?keyword="+keyword);
        Long temp = (Long) result.get("totalHits");
        page.setRows(result == null ? 0 : temp.intValue());

        return "/site/search";
    }
}
