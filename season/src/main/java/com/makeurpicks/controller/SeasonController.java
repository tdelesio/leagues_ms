package com.makeurpicks.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.makeurpicks.domain.LeagueType;
import com.makeurpicks.domain.Season;
import com.makeurpicks.service.SeasonService;

@RequestMapping(value="/seasons")
@RestController
@EnableResourceServer
public class SeasonController extends ResourceServerConfigurerAdapter {

	@Autowired
	private SeasonService seasonService;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
	        http
	        	.authorizeRequests()
	            	.antMatchers(HttpMethod.POST, "/**").authenticated()
	            	.and()
	            .authorizeRequests()
	            	.antMatchers(HttpMethod.PUT, "/**").authenticated()
	            	.and()
	            .authorizeRequests()
	                .anyRequest().permitAll();
	 }
	
	
	@RequestMapping(method=RequestMethod.GET, value="/current")
	public @ResponseBody List<Season> getCurrentSeasons()
	{
		return seasonService.getCurrentSeasons();
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="/")
//	@PreAuthorize("#userName == authentication.name")
	public @ResponseBody Season createSeason(@RequestBody Season season)
	{
		return seasonService.createSeason(season);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/")
	public @ResponseBody Season updateSeason(@RequestBody Season season)
	{
		return seasonService.updateSeason(season);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/leaguetype")
	public @ResponseBody LeagueType[] getLeagueType()
	{
		return LeagueType.values();
	}
}
