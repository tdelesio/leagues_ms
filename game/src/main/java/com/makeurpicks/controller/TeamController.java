package com.makeurpicks.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.makeurpicks.domain.Team;
import com.makeurpicks.service.TeamService;

@RequestMapping(value="/teams")
@RestController
@EnableResourceServer
public class TeamController extends ResourceServerConfigurerAdapter {

	@Autowired
	private TeamService teamService;
	
	@Override
    public void configure(HttpSecurity http) throws Exception {
        http
        	.authorizeRequests()
            	.antMatchers(HttpMethod.POST, "/**").hasRole("ADMIN")   	
            	.and()
            .authorizeRequests()
                .anyRequest().permitAll();
    }
	
	@RequestMapping(method=RequestMethod.GET, value="/leaguetype/{leagueType}")
	public @ResponseBody List<Team> getTeams(@PathVariable String leagueType)
	{
		return teamService.getTeams(leagueType);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	public @ResponseBody Team getTeamById(@PathVariable String id)
	{
		return teamService.getTeam(id);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/")
	public @ResponseBody Team createTeam(@RequestBody Team team) {
		return teamService.createTeam(team);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/pickem")
	public @ResponseBody List<Team> createPickemTeams() {
		return teamService.createTeams("pickem");
	}
}
