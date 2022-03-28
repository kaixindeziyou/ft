package com.zrulin.ftcommunity.contorller;

import com.alibaba.fastjson.JSONObject;
import com.zrulin.ftcommunity.pojo.Message;
import com.zrulin.ftcommunity.pojo.Page;
import com.zrulin.ftcommunity.pojo.User;
import com.zrulin.ftcommunity.service.MessageService;
import com.zrulin.ftcommunity.service.UserService;
import com.zrulin.ftcommunity.util.CommunityConstant;
import com.zrulin.ftcommunity.util.CommunityUtil;
import com.zrulin.ftcommunity.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.*;


/**
 * @author zrulin
 * @create 2022-03-18 10:11
 */
@Controller
public class MessageController implements CommunityConstant {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @GetMapping("/letter/list")
    public String getLetterList(Model model, Page page){
        User user = hostHolder.getUser();
        //分页信息
        page.setPath("/letter/list");
        page.setRows(messageService.findConversationCount(user.getId()));
        //查询会话列表
        List<Message> conversations = messageService.findConversations(user.getId(), page.getOffset(), page.getLimit());

        List<Map<String,Object>> conversationVoList = new ArrayList<>();
        for(Message message : conversations){
            Map<String,Object> map = new HashMap<>();
            map.put("conversation",message);
            int targetId = user.getId() == message.getFromId()?message.getToId():message.getFromId();
            User target = userService.findUserById(targetId);
            map.put("target",target);
            map.put("letterCount",messageService.findLetterCount(message.getConversationId()));
            map.put("letterUnreadCount",messageService.findLetterUnreadCount(user.getId(),message.getConversationId()));
            conversationVoList.add(map);
        }

        model.addAttribute("conversations",conversationVoList);
        //所有未读
        int unreadCount = messageService.findLetterUnreadCount(user.getId(),null);
        model.addAttribute("unreadCount",unreadCount);
        int noticeAllUnread = messageService.findNoticeUnreadCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount",noticeAllUnread);
        return "site/letter";
    }

    @GetMapping("/letter/detail/{conversationId}")
    public String getLetterDetail(@PathVariable("conversationId") String conversationId,
                                  Model model,
                                  Page page){
        //获取当前用户
        User user = hostHolder.getUser();
        //获取分页
        page.setPath("/letter/detail/" + conversationId);
        page.setRows(messageService.findLetterCount(conversationId));
        if(page.getCurrent() == null){
            page.setCurrent(page.getTotal());
        }

        //获取私信用户
        User targetUser = getLetterTarget(conversationId);
        model.addAttribute("targetUser",targetUser);


        //获取当前会话的私信列表3
        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());

        //设置已读
        List<Integer> ids = getLetterIds(letterList);
        if(!ids.isEmpty()){
            messageService.updateMessagesStatus(ids);
        }

        List<Map<String,Object>> letterVo = new ArrayList<>();
        for(Message letter: letterList){
            Map<String,Object> map = new HashMap<>();
            map.put("letter",letter);
            User fromUser = letter.getFromId() == user.getId()? user:targetUser;
            map.put("fromUser",fromUser);
            letterVo.add(map);
        }
        model.addAttribute("letters",letterVo);
        return "site/letter-detail";
    }

    private User getLetterTarget(String conversationId){
        String[] sArr = conversationId.split("_");
        int d0 = Integer.parseInt(sArr[0]);
        int d1 = Integer.parseInt(sArr[1]);
        User user = hostHolder.getUser();
        int targetId = d0 == user.getId()?d1:d0;
        return userService.findUserById(targetId);
    }

    private List<Integer> getLetterIds(List<Message> letters){
        List<Integer> ids = new ArrayList<>();
        if(letters != null){
            for(Message message : letters){
                if(hostHolder.getUser().getId() == message.getToId() && message.getStatus() == 0){
                    ids.add(message.getId());
                }
            }
        }
        return ids;
    }

    @PostMapping("/letter/add")
    @ResponseBody
    public String addMessage(String toName,String content){
        User toUser = userService.findUserByUsername(toName);
        if(toUser == null){
            return CommunityUtil.getJsonString(1,"目标用户不存在");
        }
        User user = hostHolder.getUser();
        Message message = new Message();
        message.setFromId(user.getId());
        message.setToId(toUser.getId());
        message.setStatus(0);
        String conversationId = user.getId() < toUser.getId()?user.getId()+"_"+toUser.getId():toUser.getId()+"_"+user.getId();
        message.setConversationId(conversationId);
        message.setContent(content);
        message.setCreateTime(new Date());
        messageService.addMessage(message);
        return CommunityUtil.getJsonString(0);
    }

    @GetMapping("/notice/list")
    public String NoticeList(Model model){
        User user = hostHolder.getUser();

        //分主题获取最新message以及未读数量，私信数量
        //like
        Message message = messageService.findLatestNotice(user.getId(), TOPIC_Like);
        Map<String, Object> messageVo =new HashMap<>();
        messageVo.put("message",message);
        if(message != null){
            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content,HashMap.class);
            messageVo.put("user",userService.findUserById((Integer)data.get("userId")));
            messageVo.put("entityType",data.get("entityType"));
            messageVo.put("entityId",data.get("entityId"));
            messageVo.put("postId",data.get("postId"));
            int likeNoticeCount = messageService.findNoticeCount(user.getId(), TOPIC_Like);
            int likeNoticeUnread = messageService.findNoticeUnreadCount(user.getId(), TOPIC_Like);
            messageVo.put("count",likeNoticeCount);
            messageVo.put("unreadCount",likeNoticeUnread);
        }
        model.addAttribute("likeNotice",messageVo);
        //comment
        message = messageService.findLatestNotice(user.getId(), TOPIC_COMMENT);
        messageVo =new HashMap<>();
        messageVo.put("message",message);
        if(message != null){
            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content,HashMap.class);
            messageVo.put("user",userService.findUserById((Integer)data.get("userId")));
            messageVo.put("entityType",data.get("entityType"));
            messageVo.put("entityId",data.get("entityId"));
            messageVo.put("postId",data.get("postId"));
            int commentNoticeCount = messageService.findNoticeCount(user.getId(), TOPIC_COMMENT);
            int commentNoticeUnread = messageService.findNoticeUnreadCount(user.getId(), TOPIC_COMMENT);
            messageVo.put("count",commentNoticeCount);
            messageVo.put("unreadCount",commentNoticeUnread);
        }
       model.addAttribute("commentNotice",messageVo);
        //follow
        message = messageService.findLatestNotice(user.getId(), TOPIC_FOLLOW);
        messageVo =new HashMap<>();
        messageVo.put("message",message);
        if(message != null){
            String content = HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content,HashMap.class);
            messageVo.put("user",userService.findUserById((Integer)data.get("userId")));
            messageVo.put("entityType",data.get("entityType"));
            messageVo.put("entityId",data.get("entityId"));
            int followNoticeCount = messageService.findNoticeCount(user.getId(), TOPIC_FOLLOW);
            int followNoticeUnread = messageService.findNoticeUnreadCount(user.getId(), TOPIC_FOLLOW);
            messageVo.put("count",followNoticeCount);
            messageVo.put("unreadCount",followNoticeCount);
        }
        model.addAttribute("followNotice",messageVo);

        //获取所有消息未读数量
        int noticeAllUnread = messageService.findNoticeUnreadCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount",noticeAllUnread);
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount",letterUnreadCount);
        return "site/notice";
    }

    @GetMapping("/notice/detail/{topic}")
    public String noticeDetail(
            @PathVariable("topic") String topic,
            Model model,
            Page page
    ){
        User user = hostHolder.getUser();
        //处理分页
        page.setPath("/notice/detail/"+topic);
        page.setRows(messageService.findNoticeCount(user.getId(),topic));

        //获取通知列表
        List<Message> notices = messageService.findNotices(user.getId(), topic, page.getOffset(), page.getLimit());
        List<Map<String,Object>> result = new ArrayList<>();
        if(notices != null){
            for (Message notice : notices){
                Map<String, Object> map = new HashMap<>();
                //通知
                map.put("notice",notice);
                //内容
                String content = HtmlUtils.htmlUnescape(notice.getContent());
                Map<String, Object> data = JSONObject.parseObject(content,HashMap.class);
                map.put("user",userService.findUserById((Integer) data.get("userId")));
                map.put("entityType",data.get("entityType"));
                map.put("entityId", data.get("entityId"));
                map.put("postId", data.get("postId"));
                map.put("activityId", data.get("activityId"));
                //通知作者
                map.put("fromUser",userService.findUserById(notice.getFromId()));
                result.add(map);
            }
            model.addAttribute("notices",result);

            //设置已读
            List<Integer> ids = getLetterIds(notices);
            if(!ids.isEmpty()){
                messageService.updateMessagesStatus(ids);
            }
        }
        return "site/notice-detail";
    }
}
