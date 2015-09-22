package com.makeurpicks.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.makeurpicks.domain.Season;

@FeignClient("season")
public interface SeasonClient {

	@RequestMapping(method=RequestMethod.POST, value="/season")
    public @ResponseBody Season createSeason(@RequestBody Season season);
}