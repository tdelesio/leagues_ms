package com.makeurpicks.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.makeurpicks.domain.League;

public interface LeagueClient {

	@RequestMapping(method=RequestMethod.POST, value="/league")
	public @ResponseBody League createLeague(@RequestBody League league);
}
