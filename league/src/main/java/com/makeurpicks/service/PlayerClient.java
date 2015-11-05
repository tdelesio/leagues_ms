package com.makeurpicks.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.makeurpicks.domain.PlayerResponse;

@FeignClient("gateway")
public interface PlayerClient {

	@RequestMapping(value = "/user", method=RequestMethod.GET ,produces="application/json", consumes="application/json")
    public @ResponseBody PlayerResponse getPlayer();
	
	@RequestMapping(value = "/user/{id}", method=RequestMethod.GET ,produces="application/json", consumes="application/json")
    public @ResponseBody PlayerResponse getPlayerById(@PathVariable("id") String id);
	

}
