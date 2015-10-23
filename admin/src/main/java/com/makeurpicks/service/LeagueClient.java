package com.makeurpicks.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.makeurpicks.domain.LeagueView;

@FeignClient("league")
public interface LeagueClient {

	 @RequestMapping(method=RequestMethod.GET, value="/leagues/")
		public @ResponseBody Iterable<LeagueView> getAllLeague();
}
