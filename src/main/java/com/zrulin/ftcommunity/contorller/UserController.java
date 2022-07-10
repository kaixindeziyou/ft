package com.zrulin.ftcommunity.contorller;

import com.zrulin.ftcommunity.annotation.LoginRequired;
import com.zrulin.ftcommunity.pojo.User;
import com.zrulin.ftcommunity.service.FollowService;
import com.zrulin.ftcommunity.service.LikeService;
import com.zrulin.ftcommunity.service.impl.UserServiceImpl;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author zrulin
 * @create 2022-03-13 10:42
 */
@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {
    @Autowired
    private UserServiceImpl userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String photoPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeServer;

    @Autowired
    private FollowService followService;

    /**
     * 定向到setting页面。
     * @return
     */
    @LoginRequired
    @GetMapping("/setting")
    public String getSettingPage(){
        return "site/setting";
    }

    /**
     * 修改头像
     * @param headImage
     * @param model
     * @return
     */
    @LoginRequired
    @PostMapping("/headerImage")
    public String updateHeaderUrl(MultipartFile headImage, Model model){
        if(headImage == null){
            model.addAttribute("error","您还没有选择图片");
            return"site/setting";
        }
        //获取图片的名字
        String filename = headImage.getOriginalFilename();
        //获取图片的后缀
        String suffix = filename.substring(filename.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件的格式不正确");
            return"site/setting";
        }
        //生成随机文件名
        filename = CommunityUtil.generateUUID() + suffix;
        //确定文件存放的位置
        File dest = new File(photoPath+"/"+filename);
        try {
            //存储文件
            headImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败："+e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常",e);
        }
        //更新当前用户的头像路径。（web访问路径）
        //http://localhost:8080/工程路径/
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/"+filename;
        userService.updateHeaderUrl(user.getId(),headerUrl);
        return "redirect:/index";
    }

    /**
     * 取头像
     * @param filename
     * @param response
     */

    @GetMapping("/header/{filename}")
    public void getHeader(@PathVariable("filename") String filename,
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

    /**
     * 修改密码
     * @param password
     * @param oldPassword
     * @param rePassword
     * @param model
     * @return
     */
    @LoginRequired
    @PostMapping("/updatePassword")
    public String updatePassword(String password, String oldPassword,String rePassword, Model model){
        if(!rePassword.equals(password)){
            model.addAttribute("rePasswordMsg","两次输入密码不一致");
            return"site/setting";
        }
        User user = hostHolder.getUser();
        Map<String, Object> map = userService.updatePassword(user, password, oldPassword);
        if(map == null || map.isEmpty()){
            return "redirect:/index";
        }else{
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("oldPasswordMsg",map.get("oldPasswordMsg"));
            return"site/setting";
        }
    }

    @GetMapping("/profile/{userId}")
    public String getProfilePage(@PathVariable("userId") int userId,
                                 Model model){
        //获取用户信息
        User user = userService.findUserById(userId);
        //为了避免有人恶意攻击，传错误信息进来，反复去查
        if(user == null){
            throw new RuntimeException("该用户不存在");
        }
        model.addAttribute("user",user);
        //获取被点赞数量
        int userLikeCount = likeServer.findUserLikeCount(userId);
        model.addAttribute("likeCount",userLikeCount);

        //获取关注和粉丝数量以及关注状态
        //关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount",followeeCount);
        //粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount",followerCount);
        //关注状态
        boolean status = false;
        if(hostHolder.getUser() != null){
            status = followService.followStatus(ENTITY_TYPE_USER, userId, hostHolder.getUser().getId());
        }
        model.addAttribute("followStatus",status);
        return "site/profile";

    }
}
