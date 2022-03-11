package com.zrulin.ftcommunity.service.impl;

import com.zrulin.ftcommunity.dao.UserMapper;
import com.zrulin.ftcommunity.pojo.User;
import com.zrulin.ftcommunity.service.UserService;
import com.zrulin.ftcommunity.util.CommunityConstant;
import com.zrulin.ftcommunity.util.CommunityUtil;
import com.zrulin.ftcommunity.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zrulin
 * @create 2022-03-09 18:53
 */
@Service
public class UserServiceImpl implements UserService, CommunityConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    //发邮件要包含激活码，激活码要包含域名和项目名。
    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public User findUserById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        //空值处理
        if(user == null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空");
            return map;
        }
        //验证账号
        User user1 = userMapper.selectByUsername(user.getUsername());
        if(user1 != null){
            map.put("usernameMsg","该账号已存在");
            return map;
        }
        User user2 = userMapper.selectByEmail(user.getEmail());
        if(user2 != null){
            map.put("emailMsg","该邮箱已被注册");
            return map;
        }
        //注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getSalt()+user.getPassword()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setCreateTime(new Date());
        user.setHeaderUrl(String.format("https://images.nowcoder.com/head/%dt.png",((int)(Math.random()*1000))));
        userMapper.insertUser(user);

        //发送激活邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        //http://localhost:8080/ft/activation/${user_id}/code
        //注册的时候user对象里面是没有id的，但是在insertUser的时候mybatis自动获取了id
        //基于使用的配置 mybatis.configuration.use-generated-keys=true
        //insertUser中配置了keyProperty="id"，所以mybatis会自动回填。
        String url = domain+contextPath+"/activation/"+user.getId()+"/"+user.getActivationCode();
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/activation",context);
        mailClient.sendMail(user.getEmail(),"FTCommunity 账号激活",content);
        return map;
    }

    /**
     * 激活方法
     */
    public Integer activation(Integer userId,String code){
        User user = userMapper.selectById(userId);
        if(user.getStatus() == 1){
            return ACTIVATION_REPEAT;
        }else if(user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId,1);
            return ACTIVATION_SUCCESS;
        }else{
            return ACTIVATION_FAILURE;
        }
    }
}
