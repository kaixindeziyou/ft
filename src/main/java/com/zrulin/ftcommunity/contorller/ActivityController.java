package com.zrulin.ftcommunity.contorller;

import com.zrulin.ftcommunity.annotation.LoginRequired;
import com.zrulin.ftcommunity.pojo.Activity;
import com.zrulin.ftcommunity.pojo.Comment;
import com.zrulin.ftcommunity.pojo.Page;
import com.zrulin.ftcommunity.pojo.User;
import com.zrulin.ftcommunity.service.ActivityService;
import com.zrulin.ftcommunity.service.CommentService;
import com.zrulin.ftcommunity.service.LikeServer;
import com.zrulin.ftcommunity.service.UserService;
import com.zrulin.ftcommunity.util.CommonMethod;
import com.zrulin.ftcommunity.util.CommunityConstant;
import com.zrulin.ftcommunity.util.CommunityUtil;
import com.zrulin.ftcommunity.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * 处理比赛信息
 * @author zrulin
 * @create 2022-03-25 17:01
 */
@Controller
@RequestMapping("/activity")
public class ActivityController implements CommunityConstant {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommonMethod commonMethod;

    @Value("${community.path.upload.activity}")
    private String photoPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @GetMapping("/list")
    public String findActivityList(Page page, Model model){
        //处理分页
        page.setPath("/activity/list");
        page.setRows(activityService.findActivityRows());

        List<Activity> activity = activityService.findActivity(page.getOffset(), page.getLimit());
        model.addAttribute("activity",activity);
        return "site/activity";
    }

    @GetMapping("/detail/{activityId}")
    public String findActivityDetail(Page page,
                                     Model model,
                                     @PathVariable("activityId") int activityId){
        //分页信息处理
        page.setPath("/activity/detail/"+activityId);
        page.setRows(commentService.findCommentCount(ENTITY_TYPE_ACTIVITY,activityId));

        //获取活动详情
        Activity activity = activityService.findActivityById(activityId);
        model.addAttribute("activity",activity);

        //当前用户
        User user = hostHolder.getUser();

        //评论列表
        //评论：给帖子的评论
        //回复：给评论的评论
        //评论列表
        List<Comment> commentList = commentService.findCommentByEntity(ENTITY_TYPE_ACTIVITY,activityId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> commentVOList = commonMethod.commentsAndReplays(commentList, user);
        model.addAttribute("comments",commentVOList);
        return "site/activity-detail";
    }

    @PostMapping("/add")
    @ResponseBody
    public String addActivity(
            String title,
            String content,
            Date startTime,
            Date endTime,
            String email,
            MultipartFile photo
            ){
        User user = hostHolder.getUser();
        if(user == null){
            return CommunityUtil.getJsonString(403,"您还没有登录！");
        }
        if(StringUtils.isBlank(title) || StringUtils.isBlank(content)){
            return CommunityUtil.getJsonString(403,"标题或内容不能为空！");
        }
        if(StringUtils.isBlank(email)){
            return CommunityUtil.getJsonString(403,"官方邮箱不能为空！");
        }
        if(startTime == null || endTime == null){
            return CommunityUtil.getJsonString(403,"开始时间和结束时间不能为空！");
        }
        //处理图片
        if(photo == null){
            return CommunityUtil.getJsonString(400,"您还没有上传文件！");
        }
        //获取图片的名字
        String filename = photo.getOriginalFilename();
        //获取图片的后缀
        String suffix = filename.substring(filename.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){
            return CommunityUtil.getJsonString(400,"文件的格式不正确");
        }
        //生成随机文件名
        filename = CommunityUtil.generateUUID() + suffix;
        //确定文件存放的位置
        File dest = new File(photoPath+"/"+filename);
        try {
            photo.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败："+e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常",e);
        }
        //更新当前用户的头像路径。（web访问路径）
        //http://localhost:8080/工程路径/
        String photoUrl = domain + contextPath + "/activity/photo/"+filename;
        Activity activity = new Activity();
        activity.setIssuer(user.getUsername());
        activity.setStartTime(startTime);
        activity.setEndTime(endTime);
        activity.setTitle(title);
        activity.setContent(content);
        activity.setPhoto(photoUrl);
        activity.setEmail(email);
        activity.setStatus(0);
        activity.setCommentCount(0);
        activity.setCreateTime(new Date());
        activityService.addActivity(activity);
        return CommunityUtil.getJsonString(200,"发布成功");
    }

    /**
     * 取海报
     * @param filename
     * @param response
     */
    @LoginRequired
    @GetMapping("/photo/{filename}")
    public void getPhoto(@PathVariable("filename") String filename,
                          HttpServletResponse response){
        //服务器存取路径
        filename = photoPath +"/"+ filename;
        //获取图片类型
        String type = filename.substring(filename.lastIndexOf("."));
        response.setContentType("image/"+type);
        try (   //读取文件得到输入流
                ServletOutputStream outputStream = response.getOutputStream();
                //得到输出流
                FileInputStream fileInputStream = new FileInputStream(filename);
        ){
            //，输出的时候不要一个一个字节的输出，要建立一个缓冲区，比如一次输出1024个字节，一批一批输出，效率高一点
            byte[] buffer = new byte[1024];
            int b = 0; //建立游标,不等于-1就是读到数据，等于就是没读到
            while((b = fileInputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败："+e.getMessage());
        }
    }
}
