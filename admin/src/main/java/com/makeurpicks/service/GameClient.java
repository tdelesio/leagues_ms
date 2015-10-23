package com.makeurpicks.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.makeurpicks.domain.WeekView;

@FeignClient("game")
public interface GameClient {

	@RequestMapping(method=RequestMethod.GET, value="/weeks/seasonid/{id}")
	public @ResponseBody Iterable<WeekView> getWeeksBySeason(@PathVariable String id);
}
