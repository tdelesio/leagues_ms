package com.makeurpicks.service;

import javax.ws.rs.core.MediaType;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.makeurpicks.domain.SeasonView;

@FeignClient("season")
public interface SeasonClient {

	@RequestMapping(method=RequestMethod.POST, value="/teams/pickem",produces="application/json", consumes="application/json")
	public @ResponseBody boolean createTeams();
	
	@RequestMapping(method=RequestMethod.POST, value="/seasons/",produces="application/json", consumes="application/json")
	public @ResponseBody SeasonView createSeason(@RequestBody SeasonView seasonView);
}
