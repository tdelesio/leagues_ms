package com.makeurpicks.service;

import com.makeurpicks.domain.LeagueResponse;

import feign.Param;
import feign.RequestLine;

public interface FeignLeagueClient {

//	@RequestMapping(value = "/leagues/{id}", method=RequestMethod.GET ,produces="application/json", consumes="application/json")
//    public @ResponseBody LeagueResponse getLeagueById(@PathVariable("id") String id);
	
	@RequestLine("GET /leagues/{id}")
	LeagueResponse getLeagueById(@Param("id") String id);
}
