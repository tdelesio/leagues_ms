package com.makeurpicks.controller;

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

import com.makeurpicks.domain.Week;
import com.makeurpicks.service.WeekService;

@RestController
@RequestMapping(value="/weeks")
@EnableResourceServer
public class WeekController extends ResourceServerConfigurerAdapter {

	@Autowired
	private WeekService weekService;
	
	@Override
    public void configure(HttpSecurity http) throws Exception {
        http
        	.authorizeRequests()
            	.antMatchers(HttpMethod.POST, "/**").hasRole("ADMIN")	
            	.and()
            .authorizeRequests()
                .anyRequest().permitAll();
    }
	
	@RequestMapping(method=RequestMethod.GET, value="/seasonid/{id}")
	public @ResponseBody Iterable<Week> getWeeksBySeason(@PathVariable String id)
	{
		return weekService.getWeeksBySeason(id);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/")
	public @ResponseBody Week createWeek(@RequestBody Week week)
	{
		return weekService.createWeek(week);
	}
	
}
