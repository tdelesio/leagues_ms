package com.makeurpicks.service;

import javax.ws.rs.core.MediaType;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.makeurpicks.domain.PlayerView;

@FeignClient("player")
public interface PlayerClient {

	@RequestMapping(method=RequestMethod.POST, value="/players/",produces="application/json", consumes="application/json")
	public @ResponseBody PlayerView register(@RequestBody PlayerView player);
}
