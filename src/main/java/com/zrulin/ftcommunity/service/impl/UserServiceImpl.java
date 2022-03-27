package com.zrulin.ftcommunity.service.impl;

import com.zrulin.ftcommunity.dao.UserMapper;
import com.zrulin.ftcommunity.pojo.LoginTicket;
import com.zrulin.ftcommunity.pojo.User;
import com.zrulin.ftcommunity.service.UserService;
import com.zrulin.ftcommunity.util.CommunityConstant;
import com.zrulin.ftcommunity.util.CommunityUtil;
import com.zrulin.ftcommunity.util.MailClient;
import com.zrulin.ftcommunity.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

//    @Autowired
//    private LoginTicketMapper ticketMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    //发邮件要包含激活码，激活码要包含域名和项目名。
    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public User findUserById(Integer id) {
//        return userMapper.selectById(id);
        User user = getCache(id);
        if(user == null){
            user = initCache(id);
        }
        return user;
    }

    @Override
    public User getCache(int userId) {
        String commonUserKey = RedisKeyUtil.getCommonUserKey(userId);
        return (User) redisTemplate.opsForValue().get(commonUserKey);
    }

    @Override
    public User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String commonUserKey = RedisKeyUtil.getCommonUserKey(userId);
        redisTemplate.opsForValue().set(commonUserKey,user,3600,TimeUnit.SECONDS);
        return user;
    }

    @Override
    public void clearCatch(int userId) {
        String commonUserKey = RedisKeyUtil.getCommonUserKey(userId);
        redisTemplate.delete(commonUserKey);
    }

    @Override
    public User findUserByUsername(String username) {
        return userMapper.selectByUsername(username);
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
            clearCatch(userId);
            return ACTIVATION_SUCCESS;
        }else{
            return ACTIVATION_FAILURE;
        }
    }

    @Override
    public Map<String, Object> login(String username, String password, Integer expiredSeconds) {
        Map<String, Object> result = new HashMap<>();

        //空值处理
        if(StringUtils.isBlank(username)){
            result.put("usernameMsg","用户名不能为空");
            return result;
        }
        if(StringUtils.isBlank(password)){
            result.put("passwordMsg","密码不能为空");
            return result;
        }

        //验证账号
        User user = userMapper.selectByUsername(username);
        if(user == null){
            result.put("usernameMsg","该账号不存在");
            return result;
        }
        //验证状态
        if(user.getStatus() == 0){
            result.put("usernameMsg","该账号未激活");
            return result;
        }
        //验证密码
        password = CommunityUtil.md5(user.getSalt()+password);
        if(!user.getPassword().equals(password)){
            result.put("passwordMsg","密码错误");
            return result;
        }
        //生成登录凭证,秒换成毫秒所以*1000，date里面存的是过期时间。
        LoginTicket ticket = new LoginTicket(null, user.getId(), CommunityUtil.generateUUID(), 0, new Date(System.currentTimeMillis() + expiredSeconds * 1000));
//        ticketMapper.insertTicket(ticket);
        //把凭证存入redis
        String ticketKey = RedisKeyUtil.getTicketKey(ticket.getTicket());
        redisTemplate.opsForValue().set(ticketKey,ticket);
        result.put("ticket",ticket.getTicket());
        return result;
    }


    @Override
    public void logout(String ticket) {
//        ticketMapper.updateStatus(ticket,1);
        String ticketKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(ticketKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(ticketKey,loginTicket);
    }

    @Override
    public LoginTicket findLoginTicket(String ticket) {
//        return ticketMapper.selectByTicket(ticket);
        String ticketKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(ticketKey);
    }

    @Override
    public int updateHeaderUrl(Integer userId, String url) {
        //要把删除缓存放在修改数据库之后，万一更新失败了，但是提前把缓存清除掉了也不太好
        int rows = userMapper.updateHeader(userId,url);
        clearCatch(userId);
        return rows;
    }

    @Override
    public Map<String,Object> updatePassword(User user, String password, String oldPassword) {
        Map<String,Object> result = new HashMap<>();
        // 空值处理
        if(StringUtils.isBlank(oldPassword)){
            result.put("oldPasswordMsg","原密码不能为空");
            return result;
        }
        if(StringUtils.isBlank(password)){
            result.put("passwordMsg","密码不能为空");
            return result;
        }
        //判断oldPassword 是否正确
        oldPassword = CommunityUtil.md5(user.getSalt()+oldPassword);
        if(!user.getPassword().equals(oldPassword)){
            result.put("oldPasswordMsg","原始密码输入错误");
            return result;
        }
        //加密新密码
        password = CommunityUtil.md5(user.getSalt()+password);
        userMapper.updatePassword(user.getId(),password);
        clearCatch(user.getId());
        return result;
    }
}
