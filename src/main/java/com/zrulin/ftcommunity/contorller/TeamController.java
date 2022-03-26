package com.zrulin.ftcommunity.contorller;

import com.zrulin.ftcommunity.pojo.Page;
import com.zrulin.ftcommunity.pojo.Team;
import com.zrulin.ftcommunity.pojo.User;
import com.zrulin.ftcommunity.service.TeamService;
import com.zrulin.ftcommunity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        //获取活动详情
        Team team = teamService.findTeamById(teamId);
        model.addAttribute("team",team);
        return "site/team-detail";
    }
}
