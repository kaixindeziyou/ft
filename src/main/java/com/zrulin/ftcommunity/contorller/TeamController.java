package com.zrulin.ftcommunity.contorller;

import com.zrulin.ftcommunity.pojo.Member;
import com.zrulin.ftcommunity.pojo.Page;
import com.zrulin.ftcommunity.pojo.Team;
import com.zrulin.ftcommunity.pojo.User;
import com.zrulin.ftcommunity.service.TeamService;
import com.zrulin.ftcommunity.service.UserService;
import com.zrulin.ftcommunity.util.CommunityUtil;
import com.zrulin.ftcommunity.util.HostHolder;
import com.zrulin.ftcommunity.util.RedisKeyUtil;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author zrulin
 * @create 2022-03-26 14:52
 */
@Controller
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/list")
    public String teams(Model model, Page page){
        //处理分页
        page.setPath("/team/list");
        page.setRows(teamService.findTeamRows());


        List<Team> teams = teamService.findTeams(page.getOffset(), page.getLimit());
        List<Map<String,Object>> result = new ArrayList<>();
        for (Team team : teams){
            Map<String,Object> map = new HashMap<>();
            map.put("team",team);
            User user = userService.findUserById(team.getUserId());
            map.put("user",user);
            result.add(map);
        }
        model.addAttribute("result",result);
        return "site/team";
    }

    @GetMapping("/detail/{teamId}")
    public String detailTeam(Model model,
                             @PathVariable("teamId") int teamId){
        //获取当前对象
        User loginUser = hostHolder.getUser();
        //获取活动详情
        Team team = teamService.findTeamById(teamId);
        User user = userService.findUserById(team.getUserId());
        model.addAttribute("team",team);
        model.addAttribute("user",user);

        int status = -1;
        Member member = teamService.findMember(loginUser.getId(), teamId);
        if(member != null){
            status = member.getStatus();
        }
        String temp = "";
        switch (status){
            case 0:
                temp = "审核中";
                break;
            case 1:
                temp = "已入队";
                break;
            default:
                temp = "申请入队";
        }
        model.addAttribute("status",temp);
        return "site/team-detail";
    }
    @PostMapping("/status")
    @ResponseBody
    public String memberStatus(Integer teamId){
        User user = hostHolder.getUser();
        Member member = teamService.findMember(user.getId(), teamId);
        if(member == null){
            teamService.addMember(user.getId(),teamId,0);
        }else if(member.getStatus() == 0){
            teamService.updateMemberStatus(user.getId(),teamId,2);
        }else if(member.getStatus() == 2){
            teamService.updateMemberStatus(user.getId(),teamId,0);
        }
        return CommunityUtil.getJsonString(0);
    }

    @PostMapping("/add")
    @ResponseBody
    public String addTeam(
            String name,
            Date endTime,
            Integer needPerson,
            String introduce,
            String activityName
    ){
        if(StringUtils.isBlank(name) || StringUtils.isBlank(introduce)){
            return CommunityUtil.getJsonString(404,"队名和简介不能为空！");
        }
        if (needPerson == null || endTime == null || StringUtils.isBlank(activityName)) {
            return CommunityUtil.getJsonString(404,"参数不正确");
        }
        User user = hostHolder.getUser();
        int teamId = teamService.addTeam(name,endTime,user.getId(),user.getEmail(),needPerson,introduce,activityName);
        teamService.addMember(user.getId(),teamId,1);
        return CommunityUtil.getJsonString(200,"创建成功");
    }
}
