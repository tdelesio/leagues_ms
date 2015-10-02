package com.makeurpicks.service;

import javax.ws.rs.core.MediaType;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.makeurpicks.domain.LeagueView;

@FeignClient("league")
public interface LeagueClient {

	@RequestMapping(method=RequestMethod.POST, value="/leagues/",produces="application/json", consumes="application/json")
	public @ResponseBody LeagueView createLeague(@RequestBody LeagueView league);
}
