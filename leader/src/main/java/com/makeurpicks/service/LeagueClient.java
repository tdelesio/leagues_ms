package com.makeurpicks.service;

import java.util.Set;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.makeurpicks.domain.LeagueView;
import com.makeurpicks.domain.PlayerView;

@FeignClient("league")
public interface LeagueClient {

	 @RequestMapping(method=RequestMethod.GET, value="/leagues/{id}")
	 public @ResponseBody LeagueView getLeagueById(@PathVariable String id);
	 
	 @RequestMapping(method=RequestMethod.GET, value="/leagues/player/leagueid/{leagueid}")
	 public @ResponseBody Set<PlayerView> getPlayersInLeague(@PathVariable String leagueid);
}
